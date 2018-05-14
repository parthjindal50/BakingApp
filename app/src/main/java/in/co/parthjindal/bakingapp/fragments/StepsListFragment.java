package in.co.parthjindal.bakingapp.fragments;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import in.co.parthjindal.bakingapp.utils.JsonFormatter;
import in.co.parthjindal.bakingapp.R;
import in.co.parthjindal.bakingapp.adapter.StepListAdapter;
import in.co.parthjindal.bakingapp.adapter.StepListAdapterForTwoPane;
import in.co.parthjindal.bakingapp.models.Ingredients;
import in.co.parthjindal.bakingapp.models.Step;

public class StepsListFragment extends Fragment implements StepListAdapter.OnStepListener, StepListAdapterForTwoPane.OnStepClickListener {

    private static final String STRING_RECIPE = "recipe";
    private static final String STEPS_POSITION = "STEPS_POSITION";
    private static String TWOPANE = "two_pane";
    private int stepsPosition = -1;
    private NestedScrollView scrollView;
    private ArrayList<Ingredients> mIngredients;
    private ArrayList<Step> mSteps;
    private int id = 0;
    private TextView ingredientTextView;
    private String recipeName = null;
    private boolean mTwoPane;
    private RecyclerView mStepRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        Intent intent = getActivity().getIntent();
        final Context mContext = getContext();
        if (savedInstanceState != null) {
            stepsPosition = savedInstanceState.getInt(STEPS_POSITION);
        }
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
            recipeName = intent.getStringExtra("current_recipe");
        }


        StringBuilder ingredientString = new StringBuilder();
        Double quantity;
        String ingredientName;
        String measure;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mIngredients = JsonFormatter.extractIngredientsFromJson(id, preferences);
        for (int i = 0; i < mIngredients.size(); i++) {
            ingredientName = mIngredients.get(i).getmIngredient();
            quantity = mIngredients.get(i).getmQuantity();
            measure = mIngredients.get(i).getmMeasure();
            ingredientString.append("\u25CF ");
            ingredientString.append(ingredientName);
            ingredientString.append(" (");
            ingredientString.append(quantity);
            ingredientString.append(" ");
            ingredientString.append(measure);
            ingredientString.append(")");
            ingredientString.append("\n");
        }
        ingredientTextView = view.findViewById(R.id.ingredients);
        ingredientTextView.setText(ingredientString.toString());

        mSteps = JsonFormatter.extractStepsFromJson(id);
        scrollView = view.findViewById(R.id.scrollView);
        mStepRecyclerView = view.findViewById(R.id.step_recycler_view);
        mStepRecyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mStepRecyclerView.setLayoutManager(layoutManager);
        mStepRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        if (mTwoPane) {
            StepListAdapterForTwoPane listAdapter = new StepListAdapterForTwoPane(mSteps, this, recipeName, id);
            mStepRecyclerView.setAdapter(listAdapter);
        } else {
            StepListAdapter listAdapter = new StepListAdapter(mSteps, this, recipeName, id);
            mStepRecyclerView.setAdapter(listAdapter);
        }


        if (stepsPosition != -1)
            mStepRecyclerView.smoothScrollToPosition(stepsPosition);
        else
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(0, 0);
                }
            }, 300);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
                JsonFormatter.setWedgetData(mContext, id, appWidgetManager);
                Toast.makeText(getActivity(), "widget has been added", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onClickStepSelected(View v, int position, String videoUrl, String description, String imageUrl) {
        RecipeDetailsFragment singlePageFragment =
                RecipeDetailsFragment.newInstance(videoUrl, description, imageUrl, mTwoPane);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container2, singlePageFragment, RecipeDetailsFragment.class.getSimpleName());
        ft.commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(STRING_RECIPE)) {
            mTwoPane = getArguments().getBoolean(TWOPANE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STEPS_POSITION, stepsPosition);
    }

    @Override
    public void onStepSelected(View v, int position) {
        stepsPosition = position;
    }

    public static StepsListFragment newInstance(String sRecipe, boolean twoPane) {
        StepsListFragment fragment = new StepsListFragment();
        Bundle args = new Bundle();
        args.putString(STRING_RECIPE, sRecipe);
        args.putBoolean(TWOPANE, twoPane);
        fragment.setArguments(args);
        return fragment;
    }
}