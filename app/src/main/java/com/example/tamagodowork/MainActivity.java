package com.example.tamagodowork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Correct stuff
        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        // Set default frag to the TaskListFrag
        // TODO
        // idk why it goes to the schedule frag
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new TaskListFrag()).commit();
    }

    /**
     * The listener that determines what happens when something is selected in the nav bar
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFrag = null;

                    switch (item.getItemId()) {
                        case R.id.navigation_taskList:
                            selectedFrag = new TaskListFrag();
                            break;
                        case R.id.navigation_pet:
                            selectedFrag = new PetFrag();
                            break;
                        case R.id.navigation_schedule:
                            selectedFrag = new ScheduleFrag();
                            break;
                    }

                    // replace the frame view with the fragment
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFrag).commit();

                    return true;
                }
            };
}