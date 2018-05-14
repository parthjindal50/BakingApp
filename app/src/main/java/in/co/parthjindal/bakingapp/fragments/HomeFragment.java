package in.co.parthjindal.bakingapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.co.parthjindal.bakingapp.utils.JsonFormatter;
import in.co.parthjindal.bakingapp.R;
import in.co.parthjindal.bakingapp.activities.RecipeStepsActivity;
import in.co.parthjindal.bakingapp.adapter.HomeListAdapter;
import in.co.parthjindal.bakingapp.models.Recipe;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private RecyclerView.LayoutManager layoutManager;
    private Context mContext;
    private DisplayMetrics displayMetrics;
    private List<Recipe> mRecipe;
    private SharedPreferences preferences;
    private WindowManager windowmanager;

    Unbinder unbinder;

    private int numberOfColumns() {
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    private void setData() {
        mRecipe = JsonFormatter.fetchRecipeData(getContext(), preferences);
        HomeListAdapter listAdapter = new HomeListAdapter((ArrayList<Recipe>) mRecipe, mContext, new HomeListAdapter.OnStepClickListener() {
            @Override
            public void onStepSelected(View v, int position, int id) {
                Intent intent = new Intent(mContext, RecipeStepsActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("current_recipe", mRecipe.get(position).getName());
                intent.putExtra("recipe", new GsonBuilder().create().toJson(mRecipe));
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(listAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        displayMetrics = new DisplayMetrics();
        windowmanager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        float scaleFactor = displayMetrics.density;
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;
        float widthDp = deviceWidth / scaleFactor;
        float heightDp = deviceHeight / scaleFactor;
        float smallestWidth = Math.min(widthDp, heightDp);

        mRecyclerView.setHasFixedSize(true);
        if (smallestWidth >= 600 || this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(getContext(), numberOfColumns());
        } else {
            layoutManager = new LinearLayoutManager(mContext);
        }
        mRecyclerView.setLayoutManager(layoutManager);

        setData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

