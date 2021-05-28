package com.example.tamagodowork;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;

public class EditTaskAct extends AppCompatActivity {

    private EditText editName, editDeadline, editDesc;
    private Button saveBtn, cancelBtn;
    private String key;
    private long deadline;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        // Set Views
        this.editName = findViewById(R.id.editName);
        this.editDesc = findViewById(R.id.editDesc);
        this.editDeadline = findViewById(R.id.editDeadline);
        this.editDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogView = View.inflate(getApplicationContext(), R.layout.date_time_picker, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(EditTaskAct.this).create();

                Button setDateTimeBtn = dialogView.findViewById(R.id.date_time_set_btn);
                setDateTimeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
                        TimePicker timePicker = dialogView.findViewById(R.id.time_picker);

                        deadline = LocalDateTime.of(
                                datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth(),
                                timePicker.getCurrentHour(), timePicker.getCurrentMinute()).atZone(ZoneOffset.UTC).toInstant().toEpochMilli();;
                        editDeadline.setText(Task.getDeadlineString(deadline));
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });

        // set the text in the views
        this.editName.setText(getIntent().getStringExtra("name"));
        this.editDeadline.setText(getIntent().getStringExtra("deadline"));
        this.editDesc.setText(getIntent().getStringExtra("desc"));

        this.key = getIntent().getStringExtra("key");

        this.saveBtn = findViewById(R.id.save_button);
        this.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                String desc = editDesc.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    editName.setError("Please enter a name");
                    return;
                } else if (TextUtils.isEmpty(editDesc.getText().toString())) {
                    editDeadline.setError("Please enter a deadline");
                    return;
                }

                MainActivity.userDoc.collection("Tasks").document(key)
                        .update("taskName", name,
                                "taskDeadline", deadline,
                                "taskDesc", desc)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Save Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                goBack();
            }
        });

        // Cancel Button
        this.cancelBtn = findViewById(R.id.cancel_edit_button);
        this.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    private void goBack() {
        Intent intent = new Intent(EditTaskAct.this, MainActivity.class);
        startActivity(intent);
    }
}