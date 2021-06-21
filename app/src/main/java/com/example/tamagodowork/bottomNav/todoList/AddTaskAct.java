package com.example.tamagodowork.bottomNav.todoList;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.example.tamagodowork.alarm.AlarmReceiver;
import com.google.firebase.firestore.DocumentReference;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class AddTaskAct extends AppCompatActivity {

    private EditText addName, addDeadline, addDesc;
    private long deadline;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        this.addDesc = findViewById(R.id.addDesc);
        this.addName = findViewById(R.id.addName);
        this.addDeadline = findViewById(R.id.addDeadline);
        this.addDeadline.setOnClickListener(v -> {
            final View dialogView = View.inflate(getApplicationContext(), R.layout.date_time_picker, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(AddTaskAct.this).create();

            Button setDateTimeBtn = dialogView.findViewById(R.id.date_time_set_btn);
            setDateTimeBtn.setOnClickListener(v1 -> {
                DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = dialogView.findViewById(R.id.time_picker);

                deadline = LocalDateTime.of(
                        datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(), timePicker.getCurrentMinute()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                addDeadline.setText(Task.getDeadlineString(deadline));
                alertDialog.dismiss();
            });

            alertDialog.setView(dialogView);
            alertDialog.show();
        });

        // Reminders Checkboxes
        LinearLayout remLayout = findViewById(R.id.reminders);

        Button createBtn = findViewById(R.id.create_button);
        createBtn.setOnClickListener(v -> {
            DocumentReference ref = MainActivity.userDoc.collection("Tasks").document();

            String name = addName.getText().toString();
            String desc = addDesc.getText().toString();
            String key = ref.getId();


            // check if name and deadline are filled in
            if (TextUtils.isEmpty(name)) {
                addName.setError("Please enter a name");
                return;
            } else if (TextUtils.isEmpty(addDeadline.getText().toString())) {
                addDeadline.setError("Please enter a deadline");
                return;
            }


            // Put Task in Firestore
            Task addedTask = new Task(name, deadline, desc, key);
            ref.set(addedTask);

            // reminders
            for (int i = 0; i < remLayout.getChildCount(); i++) {
                CheckBox child = (CheckBox) remLayout.getChildAt(i);

                // there are reminder alarms
                if (child.isChecked()) {
                    // time to ring
                    long alarmTime = addedTask.getAlarmTime(i);

                    // set alarm intents
                    Intent alarmIntent = new Intent(AddTaskAct.this, AlarmReceiver.class);
                    alarmIntent.putExtra("key", key);
                    alarmIntent.putExtra("taskName", name);
                    alarmIntent.putExtra("timeLeft", String.valueOf(i));
                    alarmIntent.putExtra("alarmTime", alarmTime);

                    new AlarmReceiver().setAlarm(getApplicationContext(), alarmIntent);
                }
            }

            MainActivity.backToMain(AddTaskAct.this);
        });



        // cancel button
        Button cancelBtn = findViewById(R.id.cancel_add_button);
        cancelBtn.setOnClickListener(v -> MainActivity.backToMain(AddTaskAct.this));
    }
}