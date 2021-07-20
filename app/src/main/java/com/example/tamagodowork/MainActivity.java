package com.example.tamagodowork;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;


import java.util.HashMap;
import java.util.Map;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;



public class MainActivity extends AppCompatActivity {

    private static final int FRAG_TODO_LIST = 0;
    private static final int FRAG_SCHEDULE = 1;
    private static final int FRAG_PET = 2;

    private Fragment getFrag(Integer num) {
        switch (num) {
            case FRAG_PET: return petFrag;
            case FRAG_SCHEDULE: return scheduleFrag;
            default: return todoListFrag;
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

    private final TodoListFrag todoListFrag = new TodoListFrag(this);
    private final PetFrag petFrag = new PetFrag(this);
    private final ScheduleFrag scheduleFrag = new ScheduleFrag(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Check if there is a user
        String userId = getIntent().getStringExtra("userId");
        if (userId == null) {
            startActivity(new Intent(MainActivity.this, RegisterAct.class));
            finish(); return;
        }


        // fab
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddTodoActivity.class));
            finish();
        });


        // NAVIGATION
        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        bottomNav.setOnItemSelectedListener(navListener);

        selectedFrag = getIntent().getIntExtra("selectedFrag", FRAG_TODO_LIST);
        if (selectedFrag == FRAG_PET) fab.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                getFrag(selectedFrag)).commit();
        bottomNav.setSelectedItemId(getFragId(selectedFrag));



        // XP stuff
        xpBar = findViewById(R.id.xpBar);
        this.levelView = findViewById(R.id.levelDisplay);
        this.xp = getIntent().getIntExtra("XP", 0);
        levelView.setText(getString(R.string.level, (xp/100 + 1)));
        xpBar.setProgress(xp % 100);


        // Settings
        ImageButton settings = findViewById(R.id.settings_icon);
        settings.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingsAct.class));
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


        // Tap Target Prompt - function will call a sequence of tap target prompts
        showFabPrompt();
    }

    /* fab prompt */
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    CollectionReference userData = FirebaseFirestore.getInstance().collection("Users");

    private void showFabPrompt() {
        userData.document(userId).get().addOnSuccessListener(doc -> {
            if (doc.exists()) {
                if (doc.getBoolean("didShowPrompt") == null) {
                    new MaterialTapTargetPrompt.Builder(MainActivity.this)
                            .setTarget(fab)
                            .setPrimaryText("Input your first to do")
                            .setSecondaryText("Tap the button to create your first task")
                            .setBackButtonDismissEnabled(true)
                            .setBackgroundColour(getResources().getColor(R.color.peach))
                            .setPromptStateChangeListener((prompt, state) -> {
                                // if we pressed right into the empty spot
                                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                                    // sets the key to true after first launch
                                    Map<String, Boolean> data = new HashMap<>();
                                    data.put("didShowPrompt", true);
                                    userData.document(userId).set(data, SetOptions.merge());
                                }
                            }).show();
                } else {
                    showToDo();
                }
            }
        });
    }

    private void showToDo() {
        userData.document(userId).get().addOnSuccessListener(doc -> {
            if (doc.exists()) {
                if (doc.getBoolean("didShowToDoPrompt") == null) {
                    new MaterialTapTargetPrompt.Builder(MainActivity.this)
                            .setTarget(R.id.navigation_todoList)
                            .setPrimaryText("View your ToDos for the first time!")
                            .setSecondaryText("Tap the button to view your ToDos.")
                            .setBackButtonDismissEnabled(true)
                            .setBackgroundColour(getResources().getColor(R.color.peach))
                            .setPromptStateChangeListener((prompt, state) -> {
                                // if we pressed right into the empty spot
                                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                                    // sets the key to true after first launch
                                    Map<String, Boolean> data = new HashMap<>();
                                    data.put("didShowToDoPrompt", true);
                                    userData.document(userId).set(data, SetOptions.merge());
                                }
                            }).show();
                } else {
                    showSchedule();
                }
            }
        });
    }




    private void showSchedule() {
        userData.document(userId).get().addOnSuccessListener(doc -> {
            if (doc.exists()) {
                if (doc.getBoolean("didShowSchedulePrompt") == null) {
                    new MaterialTapTargetPrompt.Builder(MainActivity.this)
                            .setTarget(R.id.navigation_schedule)
                            .setPrimaryText("View your schedule for the first time!")
                            .setSecondaryText("Tap the button to view your schedule.")
                            .setBackButtonDismissEnabled(true)
                            .setBackgroundColour(getResources().getColor(R.color.peach))
                            .setPromptStateChangeListener((prompt, state) -> {
                                // if we pressed right into the empty spot
                                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                                    // sets the key to true after first launch
                                    Map<String, Boolean> data = new HashMap<>();
                                    data.put("didShowSchedulePrompt", true);
                                    userData.document(userId).set(data, SetOptions.merge());
                                }
                            }).show();
                } else {
                    showPet();
                }
            }
        });
    }

    private void showPet() {
        userData.document(userId).get().addOnSuccessListener(doc -> {
            if (doc.exists()) {
                if (doc.getBoolean("didShowPetPrompt") == null) {
                    new MaterialTapTargetPrompt.Builder(MainActivity.this)
                            .setTarget(R.id.navigation_pet)
                            .setPrimaryText("View your pet for the first time!")
                            .setSecondaryText("Tap the button to view your pet.")
                            .setBackButtonDismissEnabled(true)
                            .setBackgroundColour(getResources().getColor(R.color.peach))
                            .setPromptStateChangeListener((prompt, state) -> {
                                // if we pressed right into the empty spot
                                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                                    // sets the key to true after first launch
                                    Map<String, Boolean> data = new HashMap<>();
                                    data.put("didShowPetPrompt", true);
                                    userData.document(userId).set(data, SetOptions.merge());
                                }
                            }).show();
                }
            }
        });
    }







    /** Bottom Navigation Bar */
    private final BottomNavigationView.OnItemSelectedListener navListener = item -> {
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
     * @param activity Context from which we are going back to MainActivity.
     */
    public static void backToMain(Activity activity) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        // check if logged in
        if (firebaseAuth.getCurrentUser() == null) {
            activity.startActivity(new Intent(activity, RegisterAct.class));
            activity.finish(); return;
        }
        String userId = firebaseAuth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userDoc = db.collection("Users").document(userId);

        userDoc.get().addOnSuccessListener(documentSnapshot -> {
            Intent intent = new Intent(activity, MainActivity.class);
            intent.putExtra("selectedFrag", documentSnapshot.get("selectedFrag", Integer.class));
            intent.putExtra("XP", documentSnapshot.get("XP", Integer.class));
            intent.putExtra("userId", userId);
            activity.startActivity(intent);
            activity.finish();
        });


    }


}