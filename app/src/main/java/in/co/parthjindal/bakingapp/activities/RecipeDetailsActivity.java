package in.co.parthjindal.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import butterknife.BindView;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import in.co.parthjindal.bakingapp.R;
import in.co.parthjindal.bakingapp.fragments.RecipeDetailsFragment;
import in.co.parthjindal.bakingapp.models.Step;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class RecipeDetailsActivity extends AppCompatActivity {

    public static final String STEPS = "STEPS";

    private List<Step> steps = new ArrayList<>();
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    Button left;
    Button right;

    int stepId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_step);
        viewPager = findViewById(R.id.view_pager);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(STEPS)) {
            String mStep = intent.getStringExtra(STEPS);
            Type type = new TypeToken<List<Step>>() {
            }.getType();
            steps.clear();
            steps = new GsonBuilder().create().fromJson(mStep, type);
            String title = intent.getExtras().getString("current_recipe");
            setTitle(title);
            stepId = intent.getIntExtra("STEP_ID", 1);
        }

        init(savedInstanceState, stepId);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stepId > 0) {
                    stepId = stepId - 1;
                    init(null, stepId);
                } else {
                    Toast.makeText(RecipeDetailsActivity.this, "No left view", Toast.LENGTH_SHORT).show();
                }
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stepId < steps.size()) {
                    stepId = stepId + 1;
                    init(null, stepId);
                } else {
                    Toast.makeText(RecipeDetailsActivity.this, "No right view", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init(Bundle savedInstanceState, int stepId) {
        if (savedInstanceState == null) {
            Step step = steps.get(stepId);
            String videoURL = step.getmVideoURL();
            String description = step.getmDescription();
            String imageUrl = step.getmThumbnailURL();
            Log.d("PagerAdapter", "" + description);
            RecipeDetailsFragment slf = RecipeDetailsFragment.newInstance(videoURL, description, imageUrl);

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, slf, RecipeDetailsFragment.class.getSimpleName());
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
