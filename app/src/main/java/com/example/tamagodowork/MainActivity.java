package com.example.tamagodowork;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {

    private ImageView settings;
    private ProgressBar xpBar;
    private TextView levelView;
    public static DocumentReference userDoc;
    public static Integer xp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Set default fragment to the task list fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new TaskListFrag()).commit();

        // Store xp values in firebase
        this.xpBar = findViewById(R.id.xpBar);
        this.levelView = findViewById(R.id.levelDisplay);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        // check if logged in
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), RegisterAct.class));
            finish();
        }

        // Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userDoc = db.collection("Users").document(firebaseAuth.getCurrentUser().getUid());

        // XP stuff
        userDoc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getApplicationContext(), "XP error", Toast.LENGTH_SHORT).show();
                }

                xp = ((Long) value.get("XP")).intValue();

                levelView.setText("Level " + (xp/100 + 1));
                xpBar.setProgress(xp % 100);
            }
        });

        // settings
        this.settings = findViewById(R.id.settings_icon);
        this.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SettingsAct.class));
                finish();
            }
        });
    }

    /**
     * Bottom Navigation Bar
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

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFrag).commit();

                    return true;
                }
            };
}