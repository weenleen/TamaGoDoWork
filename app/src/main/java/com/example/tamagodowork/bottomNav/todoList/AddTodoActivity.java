package com.example.tamagodowork.bottomNav.todoList;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.example.tamagodowork.alarm.AlarmReceiver;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class AddTodoActivity extends AppCompatActivity {

    private EditText addName, addDeadline, addDesc;
    private ImageButton addColour;
    private long deadline;

    private Integer colourId = Todo.colours[0];
    private LocalDateTime prevDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);

        Context context = getApplicationContext();



        this.addName = findViewById(R.id.addName);
        this.addDesc = findViewById(R.id.addDesc);

        this.addDeadline = findViewById(R.id.addDeadline);
        this.addDeadline.setOnClickListener(v -> {
            DialogDateTimePicker dialog = new DialogDateTimePicker(AddTodoActivity.this, prevDate);
            dialog.show(getSupportFragmentManager(), DialogDateTimePicker.TAG);
        });


        // add colour
        this.addColour = findViewById(R.id.addColour);
        ((GradientDrawable) this.addColour.getDrawable()).setColor(
                ContextCompat.getColor(context, colourId));
        this.addColour.setOnClickListener(v -> {
            DialogColourPicker dialogColourPicker = new DialogColourPicker(
                    AddTodoActivity.this, this.addColour);
            dialogColourPicker.show(getSupportFragmentManager(), DialogColourPicker.TAG);
        });



        // Reminders Checkboxes
        LinearLayout remLayout = findViewById(R.id.reminders);

        Button createBtn = findViewById(R.id.create_button);
        createBtn.setOnClickListener(v -> {
            String name = addName.getText().toString();
            String desc = addDesc.getText().toString();


            // check if name and deadline are filled in
            if (TextUtils.isEmpty(name)) {
                addName.setError("Please enter a name");
                return;
            } else if (TextUtils.isEmpty(addDeadline.getText().toString())) {
                addDeadline.setError("Please enter a deadline");
                return;
            }

            // Put Todo in Firestore
            MainActivity.userDoc.get().addOnSuccessListener(documentSnapshot -> {
                Integer key = documentSnapshot.get("lastIndex", Integer.class);
                if (key != null) key += 1;
                else key = Todo.minBound;
                MainActivity.userDoc.update("lastIndex", key);

                Todo addedTodo = new Todo(name, deadline, desc, key, colourId);
                MainActivity.userDoc.collection("Todos").document(addedTodo.getKeyStr()).set(addedTodo);

                // reminders
                for (int i = 0; i < remLayout.getChildCount(); i++) {
                    CheckBox child = (CheckBox) remLayout.getChildAt(i);

                    // there are reminder alarms
                    if (child.isChecked()) {
                        // time to ring
                        long alarmTime = addedTodo.getAlarmTime(i);

                        // set alarm intents
                        Intent alarmIntent = new Intent(AddTodoActivity.this, AlarmReceiver.class);
                        alarmIntent.putExtra("key", key);
                        alarmIntent.putExtra("name", name);
                        alarmIntent.putExtra("alarmType", i);
                        alarmIntent.putExtra("alarmTime", alarmTime);

                        new AlarmReceiver().setAlarm(AddTodoActivity.this, alarmIntent);
                    }
                }
            });

            MainActivity.backToMain(AddTodoActivity.this);
        });


        // cancel button
        Button cancelBtn = findViewById(R.id.cancel_add_button);
        cancelBtn.setOnClickListener(v -> MainActivity.backToMain(AddTodoActivity.this));
    }

    public void setColourId(int colourId) {
        this.colourId = colourId;
    }

    public void setDeadline(LocalDateTime updated) {
        this.deadline = updated.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.addDeadline.setText(Todo.getDeadlineString(deadline));
        this.prevDate = updated;
    }
}