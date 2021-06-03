package com.example.tamagodowork;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


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

        this.key = getIntent().getStringExtra("key");
        final String deadlineStr = getIntent().getStringExtra("deadline");

        // set the text in the views
        this.editName.setText(getIntent().getStringExtra("name"));
        this.editDeadline.setText(deadlineStr);
        this.editDesc.setText(getIntent().getStringExtra("desc"));

        // set deadline to previous deadline
        LocalDateTime prevDate = LocalDateTime.parse(deadlineStr,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm") );
        deadline = prevDate.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();

        // Check reminders Checkboxes
        LinearLayout remLayout = findViewById(R.id.reminders);
        DocumentReference remRef = MainActivity.userDoc.collection("Reminders").document(key);
        remRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                if (!task.isSuccessful()) return;

                Map<String, Object> data = task.getResult().getData();
                if (data == null) return;

                for (int i = 0; i < remLayout.getChildCount(); i++) {
                    if (data.get(String.valueOf(i)) != null) { // if there is an alarm
                        ((CheckBox) remLayout.getChildAt(i)).setChecked(true);
                    }
                }
            }
        });


        // change deadline
        this.editDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogView = View.inflate(getApplicationContext(), R.layout.date_time_picker, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(EditTaskAct.this).create();

                DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = dialogView.findViewById(R.id.time_picker);

                // make pickers display previous deadline
                datePicker.updateDate(prevDate.getYear(), prevDate.getMonthValue() - 1, prevDate.getDayOfMonth());
                timePicker.setCurrentHour(prevDate.getHour());
                timePicker.setCurrentMinute(prevDate.getMinute());

                // set date button
                Button setDateTimeBtn = dialogView.findViewById(R.id.date_time_set_btn);
                setDateTimeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deadline = LocalDateTime.of(
                                datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth(),
                                timePicker.getCurrentHour(), timePicker.getCurrentMinute())
                                .atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
                        editDeadline.setText(Task.getDeadlineString(deadline));
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });

        // save button
        this.saveBtn = findViewById(R.id.save_button);
        this.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                remRef.delete();
                Map<String, Object> tmp = new HashMap<>();
                for (int i = 0; i < remLayout.getChildCount(); i++) {
                    CheckBox child = (CheckBox) remLayout.getChildAt(i);
                    if (child.isChecked()) {
                        tmp.put(String.valueOf(i), "edit alarm");
                    }
                }
                if (!tmp.isEmpty()) remRef.set(tmp);

                // update tasks in Firestore
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