package com.example.fitnesstrackerlite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_food:
                    toolbar.setTitle("Food");
                    fragment = new FoodFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_drink:
                    toolbar.setTitle("Drink");
                    fragment = new DrinkFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_exercise:
                    toolbar.setTitle("Exercise");
                    fragment = new ExerciseFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_sleep:
                    toolbar.setTitle("Sleep");
                    fragment = new SleepFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the toolbar and set it as the app bar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //load the first fragment
        getSupportActionBar().setTitle("Food");
        loadFragment(new FoodFragment());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new FoodFragment());
    }

    private void loadFragment(Fragment fragment){
        if (fragment != null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, fragment);
            transaction.commit();
        }
    }
}
