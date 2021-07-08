package com.example.tamagodowork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
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

    public static final int FRAG_TODO_LIST = 0;
    public static final int FRAG_SCHEDULE = 1;
    public static final int FRAG_PET = 2;

    private static Fragment getFrag(Integer num) {
        switch (num) {
            case FRAG_PET: return new PetFrag();
            case FRAG_SCHEDULE: return new ScheduleFrag();
            default: return new TodoListFrag();
        }
    }

    private static int getFragId(Integer num) {
        switch (num) {
            case FRAG_PET: return R.id.navigation_pet;
            case FRAG_SCHEDULE: return R.id.navigation_schedule;
            default: return R.id.navigation_todoList;
        }
    }

    public static DocumentReference userDoc;


    private int xp;
    public ProgressBar xpBar;
    private TextView levelView;
    private FloatingActionButton fab;

    private int selectedFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Store xp values in firebase
        xpBar = findViewById(R.id.xpBar);
        this.levelView = findViewById(R.id.levelDisplay);

        // fab
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), AddTodoActivity.class));
            finish();
        });


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

        selectedFrag = getIntent().getIntExtra("selectedFrag", FRAG_TODO_LIST);
        if (selectedFrag == FRAG_PET) fab.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                getFrag(selectedFrag)).commit();
        bottomNav.setSelectedItemId(getFragId(selectedFrag));


        // XP stuff
        this.xp = getIntent().getIntExtra("XP", 0);
        levelView.setText(getString(R.string.level, (xp/100 + 1)));
        xpBar.setProgress(xp % 100);


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
    }


    /** Bottom Navigation Bar */
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        int tmp = item.getItemId();

        if (tmp == R.id.navigation_pet) {
            fab.setVisibility(View.GONE);
            selectedFrag = FRAG_PET;
        } else if (tmp == R.id.navigation_schedule) {
            fab.setVisibility(View.VISIBLE);
            selectedFrag = FRAG_SCHEDULE;
        } else {
            fab.setVisibility(View.VISIBLE);
            selectedFrag = FRAG_TODO_LIST;
        }

        userDoc.update("selectedFrag", selectedFrag);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                getFrag(selectedFrag)).commit();
        return true;
    };

    public int getXP() { return xp; }

    /**
     * A method that increments the amount of xp locally and in Firestore
     * after a task is completed. We can adjust this method if we ever want to
     * change how XP is calculated.
     */
    public void incrementXP() {
        xp += 10;
        userDoc.update("XP", xp);
        int level = xp/100 + 1;
        levelView.setText(getString(R.string.level, level));
        ObjectAnimator.ofInt(xpBar, "progress", xp % 100)
                .setDuration(200).start();

        if (xp % 100 == 0) {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
            dialog.setMessage(this.getString(R.string.level_up_message, level));
            dialog.show();
        }
    }

    /**
     * A method that updates the amount of xp in Firestore only.
     *
     * @param newXP The new value of xp.
     */
    public static void setXP(int newXP) {
        userDoc.update("XP", newXP);
    }

    /**
     * Use this to go back to MainActivity.
     *
     * @param context Context from which we are going back to MainActivity.
     */
    public static void backToMain(Activity context) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        // check if logged in
        if (firebaseAuth.getCurrentUser() == null) {
            context.startActivity(new Intent(context, RegisterAct.class));
            context.finish();
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDoc = db.collection("Users").document(firebaseAuth.getCurrentUser().getUid());

        userDoc.get().addOnSuccessListener(documentSnapshot -> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("selectedFrag", documentSnapshot.get("selectedFrag", Integer.class));
            intent.putExtra("XP", documentSnapshot.get("XP", Integer.class));
            context.startActivity(intent);
            context.finish();
        });
    }
}