package com.example.tamagodowork.bottomNav.schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.example.tamagodowork.bottomNav.taskList.Task;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class AddEventActivity extends AppCompatActivity {

    private EditText eventName, eventStart, eventEnd, eventDesc;
    private long start, end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // initialising
        this.eventName = findViewById(R.id.eventName);
        this.eventDesc = findViewById(R.id.eventDesc);


        // start
        this.eventStart = findViewById(R.id.eventStart);
        this.eventStart.setOnClickListener(v -> {
            final View dialogView = View.inflate(getApplicationContext(), R.layout.date_time_picker, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(AddEventActivity.this).create();

            Button setDateTimeBtn = dialogView.findViewById(R.id.date_time_set_btn);
            setDateTimeBtn.setOnClickListener(v1 -> {
                DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = dialogView.findViewById(R.id.time_picker);

                start = LocalDateTime.of(
                        datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(), timePicker.getCurrentMinute()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                // TODO
                eventStart.setText(Task.getDeadlineString(start));
                alertDialog.dismiss();
            });

            alertDialog.setView(dialogView);
            alertDialog.show();
        });



        // end
        this.eventEnd = findViewById(R.id.eventEnd);
        this.eventEnd.setOnClickListener(v -> {
            final View dialogView = View.inflate(getApplicationContext(), R.layout.date_time_picker, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(AddEventActivity.this).create();

            Button setDateTimeBtn = dialogView.findViewById(R.id.date_time_set_btn);
            setDateTimeBtn.setOnClickListener(v1 -> {
                DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = dialogView.findViewById(R.id.time_picker);

                end = LocalDateTime.of(
                        datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(), timePicker.getCurrentMinute()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                // TODO
                eventEnd.setText(Task.getDeadlineString(end));
                alertDialog.dismiss();
            });

            alertDialog.setView(dialogView);
            alertDialog.show();
        });


        // create Button
        Button createBtn = findViewById(R.id.create_button);
        createBtn.setOnClickListener(v -> {
            String name = eventName.getText().toString();
            String desc = eventDesc.getText().toString();

            if (TextUtils.isEmpty(name)) {
                eventName.setError("Please enter a name");
                return;
            }
            if (TextUtils.isEmpty(eventStart.getText().toString())) {
                eventStart.setError("Please enter a name");
                return;
            }
            if (TextUtils.isEmpty(eventEnd.getText().toString())) {
                eventEnd.setError("Please enter a duration");
                return;
            }

            MainActivity.backToMain(AddEventActivity.this);
            finish();
        });

        // cancel button
        Button cancelBtn = findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(v -> {
            MainActivity.backToMain(AddEventActivity.this);
            finish();
        });
    }
}