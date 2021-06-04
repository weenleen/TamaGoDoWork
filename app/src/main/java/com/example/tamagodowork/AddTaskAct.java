package com.example.tamagodowork;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tamagodowork.alarm.AlarmReceiver;
import com.google.firebase.firestore.DocumentReference;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;


public class AddTaskAct extends AppCompatActivity {

    private EditText addName, addDeadline, addDesc;
    private Button createBtn, cancelBtn;
    private long deadline;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        this.addDesc = findViewById(R.id.addDesc);
        this.addName = findViewById(R.id.addName);
        this.addDeadline = findViewById(R.id.addDeadline);
        this.addDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogView = View.inflate(getApplicationContext(), R.layout.date_time_picker, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(AddTaskAct.this).create();

                Button setDateTimeBtn = dialogView.findViewById(R.id.date_time_set_btn);
                setDateTimeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
                        TimePicker timePicker = dialogView.findViewById(R.id.time_picker);

                        deadline = LocalDateTime.of(
                                datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth(),
                                timePicker.getCurrentHour(), timePicker.getCurrentMinute()).atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
                        addDeadline.setText(Task.getDeadlineString(deadline));
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });

        // Reminders Checkboxes
        LinearLayout remLayout = findViewById(R.id.reminders);

        this.createBtn = findViewById(R.id.create_button);
        this.createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        // set alarm intents
                        Intent alarmIntent = new Intent(AddTaskAct.this, AlarmReceiver.class);
                        alarmIntent.putExtra("key", key);
                        alarmIntent.putExtra("timeLeft", String.valueOf(i));
                        PendingIntent pendingIntent = PendingIntent
                                .getBroadcast(AddTaskAct.this, 0, alarmIntent, 0);

                        // time to ring
                        Long alarmTime = addedTask.getAlarmTime(i);

                        // alarm manager
                        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, alarmTime, pendingIntent);

                        ref.collection("Reminders").document(String.valueOf(i))
                                .set(Map.of("alarmTime", alarmTime));
                    }
                }

                MainActivity.backToMain(AddTaskAct.this);
            }
        });



        // cancel button
        this.cancelBtn = findViewById(R.id.cancel_add_button);
        this.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.backToMain(AddTaskAct.this);
            }
        });
    }
}