package com.example.tamagodowork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // WHAT IDK
    RecyclerView taskListView;
    ArrayList<Task> taskList;
    TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // um idk
        this.taskListView = findViewById(R.id.taskListView);
        this.taskList = new ArrayList<Task>();

        // TESTING
        this.taskList.add(new Task("Help", "Help Desc.", new Date()));

        this.taskAdapter = new TaskAdapter(this, this.taskList);
        this.taskListView.setAdapter(this.taskAdapter);
        this.taskListView.setLayoutManager(new LinearLayoutManager(this));


        // Correct stuff
        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new TaskListFrag()).commit();
    }

    // Correct ish stuff
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

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFrag).commit();

                    return true;
                }
            };
}