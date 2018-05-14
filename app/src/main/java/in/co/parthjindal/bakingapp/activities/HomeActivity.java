package in.co.parthjindal.bakingapp.activities;

import in.co.parthjindal.bakingapp.R;
import in.co.parthjindal.bakingapp.fragments.HomeFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private void initRecipeFragment(){
        HomeFragment rf = new HomeFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment_container, rf, HomeFragment.class.getSimpleName());
        ft.commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecipeFragment();
    }
}
