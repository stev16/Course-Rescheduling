package com.example.coursereschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.coursereschedule.Fragment.fridayFragment;
import com.example.coursereschedule.Fragment.mondayFragment;
import com.example.coursereschedule.Fragment.thursdayFragment;
import com.example.coursereschedule.Fragment.tuesdayFragment;
import com.example.coursereschedule.Fragment.wednesdayFragment;
import com.example.coursereschedule.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class timetableNavigation extends AppCompatActivity {


    ActionBar actionBar;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_navigation);


        actionBar = getSupportActionBar();
        actionBar.setTitle("Timetable");

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        // When we open the application first
        // time the fragment should be shown to the user
        // in this case it is home fragment
        mondayFragment mondayFragment = new mondayFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, mondayFragment, "");
        fragmentTransaction.commit();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.mondayMenu:
                    mondayFragment fragment = new mondayFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, fragment, "");
                    fragmentTransaction.commit();
                    return true;

                case R.id.tuesdayMenu:

                    tuesdayFragment fragment1 = new tuesdayFragment();
                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.content, fragment1);
                    fragmentTransaction1.commit();
                    return true;

                case R.id.wednesdayMenu:

                    wednesdayFragment fragment2 = new wednesdayFragment();
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.content, fragment2, "");
                    fragmentTransaction2.commit();
                    return true;

                case R.id.thursdayMenu:

                    thursdayFragment listFragment = new thursdayFragment();
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.content, listFragment, "");
                    fragmentTransaction3.commit();
                    return true;

                case R.id.fridayMenu:

                    fridayFragment fragment4 = new fridayFragment();
                    FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction4.replace(R.id.content, fragment4, "");
                    fragmentTransaction4.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                Intent intent =new Intent(timetableNavigation.this, homePage.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}