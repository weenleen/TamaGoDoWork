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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tamagodowork.authentication.RegisterAct;
import com.example.tamagodowork.bottomNav.pet.PetFrag;
import com.example.tamagodowork.bottomNav.todoList.AddTodoActivity;
import com.example.tamagodowork.bottomNav.todoList.ScheduleFrag;
import com.example.tamagodowork.bottomNav.todoList.TodoListFrag;
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
            int selectedIndex = R.id.navigation_todoList;
            if (documentSnapshot != null && documentSnapshot.exists()) {
                Integer tmp = documentSnapshot.get("selectedFrag", Integer.class);
                if (tmp != null) selectedIndex = tmp;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    getFrag(selectedIndex)).commit();
            bottomNav.setSelectedItemId(selectedIndex);
        });



        // XP stuff
        userDoc.addSnapshotListener((value, error) -> {
            if (error != null || value == null) return;

            Integer tmp = value.get("XP", Integer.class);
            if (tmp == null) {
                MainActivity.setXP(0);
                return;
            }

            xp = tmp;
            levelView.setText(getString(R.string.level, (xp/100 + 1)));
            ObjectAnimator.ofInt(xpBar, "progress", xp % 100)
                    .setDuration(200)
                    .start();
        });



        // Settings
        ImageButton settings = findViewById(R.id.settings_icon);
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
            startActivity(new Intent(getApplicationContext(), AddTodoActivity.class));
            finish();
        });
    }

    private static Fragment getFrag(int num) {
        if (num == R.id.navigation_pet) {
            return new PetFrag();
        } else if (num == R.id.navigation_schedule) {
            return new ScheduleFrag();
        } else {
            return new TodoListFrag();
        }
    }


    /** Bottom Navigation Bar */
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        int tmp = item.getItemId();
        Fragment selectedFrag;

        if (tmp == R.id.navigation_pet) {
            fab.setVisibility(View.GONE);
            selectedFrag = new PetFrag();
        } else if (tmp == R.id.navigation_schedule) {
            fab.setVisibility(View.VISIBLE);
            selectedFrag = new ScheduleFrag();
        } else {
            fab.setVisibility(View.VISIBLE);
            selectedFrag = new TodoListFrag();
        }

        userDoc.update("selectedFrag", tmp);
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