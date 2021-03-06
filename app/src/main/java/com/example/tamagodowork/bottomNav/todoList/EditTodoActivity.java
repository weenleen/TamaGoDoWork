package com.example.tamagodowork.bottomNav.todoList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.example.tamagodowork.alarm.AlarmReceiver;
import com.example.tamagodowork.authentication.RegisterAct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class EditTodoActivity extends AppCompatActivity {

    private EditText editName, editDeadline, editDesc;
    private ImageButton editColour;

    private int key;
    private long deadline;
    private String colourKey;

    private LocalDateTime prevDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_edit);

        Context context = getApplicationContext();

        // Set Views
        this.editName = findViewById(R.id.editName);
        this.editDesc = findViewById(R.id.editDesc);
        this.editDeadline = findViewById(R.id.editDeadline);

        Todo todo = getIntent().getParcelableExtra(Todo.parcelKey);

        // set the text in the views
        this.editName.setText(todo.getName());
        this.editDeadline.setText(todo.getDeadlineString());
        this.editDesc.setText(todo.getDesc());


        // get values
        this.deadline = todo.getDeadline();
        this.key = todo.getKey();


        // Check reminders Checkboxes
        LinearLayout remLayout = findViewById(R.id.reminders);
        for (int i = 0; i < remLayout.getChildCount(); i++) {
            if (todo.getReminders().get(i)) ((CheckBox) remLayout.getChildAt(i)).setChecked(true);
        }


        // change deadline
        prevDate = todo.getDateTime();
        this.editDeadline.setOnClickListener(v -> {
            DialogDateTimePicker dialog = new DialogDateTimePicker(EditTodoActivity.this, prevDate);
            dialog.show(getSupportFragmentManager(), DialogDateTimePicker.TAG);
        });


        // edit Colour
        int color;
        try {
            color = ContextCompat.getColor(context, todo.getColourId());
            colourKey = todo.getColourKey();
        } catch (Exception e) {
            todo.setColourKey("PEACH");
            colourKey = "PEACH";
            color = ContextCompat.getColor(context, todo.getColourId());
        }
        this.editColour = findViewById(R.id.editColour);
        ((GradientDrawable) this.editColour.getDrawable()).setColor(color);
        this.editColour.setOnClickListener(v -> {
            DialogColourPicker dialogColourPicker = new DialogColourPicker(
                    EditTodoActivity.this, this.editColour);
            dialogColourPicker.show(getSupportFragmentManager(), DialogColourPicker.TAG);
        });



        // save button
        Button saveBtn = findViewById(R.id.save_button);
        saveBtn.setOnClickListener(v -> {
            String name = editName.getText().toString();
            String desc = editDesc.getText().toString();

            if (TextUtils.isEmpty(name)) {
                editName.setError("Please enter a name");
                return;
            } else if (TextUtils.isEmpty(editDeadline.getText().toString())) {
                editDeadline.setError("Please enter a deadline");
                return;
            }


            // update alarms
            List<Boolean> updatedRem= new ArrayList<>();
            Todo updatedTodo = new Todo(name, deadline, desc, key, colourKey, updatedRem);
            for (int i = 0; i < remLayout.getChildCount(); i++) {
                CheckBox child = (CheckBox) remLayout.getChildAt(i);
                Intent alarmIntent = new Intent(EditTodoActivity.this, AlarmReceiver.class);
                alarmIntent.putExtra("key", key);
                alarmIntent.putExtra("name", name);
                alarmIntent.putExtra("alarmType", i);

                long alarmTime = updatedTodo.getAlarmTime(i);
                int requestCode = todo.getReqCode(i);
                alarmIntent.putExtra("alarmTime", alarmTime);

                if (child.isChecked()) {
                    updatedRem.add(true);
                    if (todo.hasReminderAt(i)) {
                        new AlarmReceiver().setOrCancel(EditTodoActivity.this, alarmIntent);
                    } else {
                        new AlarmReceiver().setAlarm(EditTodoActivity.this, alarmIntent);
                    }
                } else {
                    updatedRem.add(false);
                    new AlarmReceiver().cancelAlarmIfExists(EditTodoActivity.this, requestCode, alarmIntent);
                }
            }

            // update todos in Firestore
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            // check if logged in
            if (firebaseAuth.getCurrentUser() == null) {
                startActivity(new Intent(EditTodoActivity.this, RegisterAct.class));
                finish(); return;
            }
            String userId = firebaseAuth.getCurrentUser().getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userDoc = db.collection("Users").document(userId);
            userDoc.collection("Todos").document(String.valueOf(key))
                    .update("name", name,
                            "deadline", deadline,
                            "desc", desc,
                            "colourKey", colourKey,
                            "reminders", updatedRem)
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Save Failed", Toast.LENGTH_SHORT).show())
                    .addOnSuccessListener(unused -> MainActivity.backToMain(EditTodoActivity.this));
        });


        // Cancel Button
        Button cancelBtn = findViewById(R.id.cancel_edit_button);
        cancelBtn.setOnClickListener(v -> MainActivity.backToMain(EditTodoActivity.this));
    }

    public void setColourKey(String colourKey) {
        this.colourKey = colourKey;
    }

    public void setDeadline(LocalDateTime updated) {
        this.deadline = updated.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.editDeadline.setText(Todo.getDeadlineString(deadline));
        this.prevDate = updated;
    }
}