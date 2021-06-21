package com.example.tamagodowork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.animation.ObjectAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamagodowork.authentication.RegisterAct;
import com.example.tamagodowork.bottomNav.pet.PetFrag;
import com.example.tamagodowork.bottomNav.schedule.AddEventActivity;
import com.example.tamagodowork.bottomNav.schedule.ScheduleFrag;
import com.example.tamagodowork.bottomNav.taskList.AddTaskAct;
import com.example.tamagodowork.bottomNav.taskList.TaskListFrag;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    public static DocumentReference userDoc;
    private static Integer xp = 0;

    public ProgressBar xpBar;
    private TextView levelView;
    private int selectedIndex = 0;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        // Store xp values in firebase
        xpBar = findViewById(R.id.xpBar);
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


        // NAVIGATION
        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Set default fragment to the task list fragment
        userDoc.get().addOnSuccessListener(documentSnapshot -> {
            selectedIndex = 0;
            if (documentSnapshot != null) {
                Integer tmp = documentSnapshot.get("selectedFrag", Integer.class);
                if (tmp != null) selectedIndex = tmp;
            }

            if (selectedIndex == 1) {
                this.fab.setVisibility(View.GONE);
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    getFrag(selectedIndex)).commit();
        });



        // XP stuff
        userDoc.addSnapshotListener((value, error) -> {
            if (error != null || value == null) {
                Toast.makeText(getApplicationContext(), "XP error", Toast.LENGTH_SHORT).show();
                return;
            }

            Integer tmp = value.get("XP", Integer.class);
            if (tmp == null) {
                MainActivity.setXP(0);
                return;
            }

            xp = tmp;
            levelView.setText("Level " + (xp/100 + 1));
            ObjectAnimator.ofInt(xpBar, "progress", xp % 100)
                    .setDuration(200)
                    .start();
        });



        // Settings
        ImageView settings = findViewById(R.id.settings_icon);
        settings.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SettingsAct.class));
            finish();
        });



        // set a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "tamagodowork",
                    "tamagodoworkReminderChannel",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Reminder Channel");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        // fab
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), AddTaskAct.class));
            finish();
        });
    }

    private static Fragment getFrag(int num) {
        switch (num) {
            case 1:
                return new PetFrag();
            case 2:
                return new ScheduleFrag();
            default:
                return new TaskListFrag();
        }
    }


    /** Bottom Navigation Bar */
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        int tmp = item.getItemId();
        Fragment selectedFrag;

        if (tmp == R.id.navigation_pet) {
            fab.setVisibility(View.GONE);
            selectedIndex = 1;
            selectedFrag = new PetFrag();
        } else if (tmp == R.id.navigation_schedule) {
            fab.setVisibility(View.VISIBLE);
            selectedIndex = 2;
            selectedFrag = new ScheduleFrag();
        } else {
            selectedIndex = 0;
            fab.setVisibility(View.VISIBLE);
            selectedFrag = new TaskListFrag();
        }

        userDoc.update("selectedFrag", selectedIndex);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFrag).commit();
        return true;
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