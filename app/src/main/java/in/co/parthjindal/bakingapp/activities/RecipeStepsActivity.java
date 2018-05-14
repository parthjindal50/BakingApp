package in.co.parthjindal.bakingapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import butterknife.BindBool;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.co.parthjindal.bakingapp.R;
import in.co.parthjindal.bakingapp.fragments.HomeFragment;
import in.co.parthjindal.bakingapp.fragments.StepsListFragment;


public class RecipeStepsActivity extends AppCompatActivity {

    private String sRecipe;
    private static final String RECIPE_NAME = "recipe_name";
    private static final String RECIPE_ID = "recipe_id";
    private static final int DEFAULT_RECIPE_ID = 1;


    @BindBool(R.bool.two_pane_mode)
    boolean twoPaneMode;
    Unbinder unbinder;

    private void init(Bundle savedInstanceState, String recipe) {
        if (savedInstanceState == null) {
            StepsListFragment slf = StepsListFragment.newInstance(recipe, twoPaneMode);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, slf, HomeFragment.class.getSimpleName());
            ft.commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);
        unbinder = ButterKnife.bind(this);
        if (getIntent() != null) {
            int id = getIntent().getIntExtra(RECIPE_ID, DEFAULT_RECIPE_ID);
            sRecipe = getIntent().getExtras().getString("recipe");
            String title = getIntent().getExtras().getString(RECIPE_NAME);
            setTitle(title);
        }

        if (twoPaneMode) {
            init(savedInstanceState, sRecipe);

        } else {
            init(savedInstanceState, sRecipe);

        }
    }


}

