package com.example.tamagodowork;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamagodowork.pet.CreatePetActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {

    public static DocumentReference userDoc;
    private static Integer xp = 0;

    private ImageView settings;
    private ProgressBar xpBar;
    private TextView levelView;

    private NotificationChannel channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        // test
//        startActivity(new Intent(MainActivity.this, CreatePetActivity.class));
//        finish();



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
                    return;
                }

                Long tmp = (Long) value.get("XP");
                if (tmp == null) {
                    MainActivity.setXP(0);
                    return;
                }

                xp = tmp.intValue();

                levelView.setText("Level " + (xp/100 + 1));
                xpBar.setProgress(xp % 100);
            }
        });

        /** Settings */
        this.settings = findViewById(R.id.settings_icon);
        this.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SettingsAct.class));
                finish();
            }
        });

        // set a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.channel = new NotificationChannel(
                    "tamagodowork",
                    "tamagodoworkReminderChannel",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Reminder Channel");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /** Bottom Navigation Bar */
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFrag = new TaskListFrag();

                    switch (item.getItemId()) {
                        case R.id.navigation_taskList:
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

    /**
     * A method that updates the amount of xp locally and in Firestore.
     *
     * @param newXP The new value of xp.
     */
    public static void setXP(int newXP) {
        xp = newXP;
        userDoc.update("XP", newXP);
    }

    /**
     * A method that increments the amount of xp locally and in Firestore
     * after a task is completed. We can adjust this method if we ever want to
     * change how XP is calculated.
     */
    public static void incrXP() {
        xp += 10;
        userDoc.update("XP", xp);
    }

    /**
     * Use this to go back to MainActivity
     *
     * @param context Context from which we are going back to MainActivity.
     */
    public static void backToMain(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }
}