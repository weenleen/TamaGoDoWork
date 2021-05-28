package com.example.tamagodowork;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Calendar;


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
                                timePicker.getCurrentHour(), timePicker.getCurrentMinute()).atZone(ZoneOffset.UTC).toInstant().toEpochMilli();;
                        addDeadline.setText(Task.getDeadlineString(deadline));
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });

        this.createBtn = findViewById(R.id.create_button);
        this.createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference ref = MainActivity.userDoc.collection("Tasks").document();

                String name = addName.getText().toString();
                String desc = addDesc.getText().toString();
                String key = ref.getId();

                if (TextUtils.isEmpty(name)) {
                    addName.setError("Please enter a name");
                    return;
                } else if (TextUtils.isEmpty(addDeadline.getText().toString())) {
                    addDeadline.setError("Please enter a deadline");
                    return;
                }

                ref.set(new Task(name, deadline, desc, key));

                goBack();
            }
        });

        this.cancelBtn = findViewById(R.id.cancel_add_button);
        this.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    private void goBack() {
        Intent intent = new Intent(AddTaskAct.this, MainActivity.class);
        startActivity(intent);
    }
}