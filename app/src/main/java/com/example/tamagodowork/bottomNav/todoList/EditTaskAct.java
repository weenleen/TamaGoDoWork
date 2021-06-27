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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class EditTaskAct extends AppCompatActivity {

    private EditText editName, editDeadline, editDesc;
    private ImageButton editColour;

    private String key;
    private long deadline;
    private int colourId;

    private LocalDateTime prevDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);

        Context context = getApplicationContext();

        // Set Views
        this.editName = findViewById(R.id.editName);
        this.editDesc = findViewById(R.id.editDesc);
        this.editDeadline = findViewById(R.id.editDeadline);

        Task task = getIntent().getParcelableExtra(Task.parcelKey);

        // set the text in the views
        this.editName.setText(task.getTaskName());
        this.editDeadline.setText(task.getDeadlineString());
        this.editDesc.setText(task.getTaskDesc());
        this.key = task.getKey();

        // set deadline to previous deadline
        this.deadline = task.getTaskDeadline();

        // Check reminders Checkboxes
        LinearLayout remLayout = findViewById(R.id.reminders);
        CollectionReference remRef = MainActivity.userDoc.collection("Tasks")
                .document(key).collection("Reminders");
        remRef.get().addOnCompleteListener(t -> {
            if (!t.isSuccessful() || t.getResult() == null) return;

            // alarmId
            for (QueryDocumentSnapshot doc : t.getResult()) {
                ((CheckBox) remLayout.getChildAt(Integer.parseInt(doc.getId()))).setChecked(true);
            }
        });


        // change deadline
        prevDate = task.getDateTime();
        this.editDeadline.setOnClickListener(v -> {
            DialogDateTimePicker dialog = new DialogDateTimePicker(EditTaskAct.this, prevDate);
            dialog.show(getSupportFragmentManager(), DialogDateTimePicker.TAG);
        });


        // edit Colour
        this.colourId = task.getColourId();
        this.editColour = findViewById(R.id.editColour);
        ((GradientDrawable) this.editColour.getDrawable()).setColor(
                ContextCompat.getColor(context, colourId));
        this.editColour.setOnClickListener(v -> {
            DialogColourPicker dialogColourPicker = new DialogColourPicker(
                    EditTaskAct.this, this.editColour);
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

            Task updatedTask = new Task(name, deadline, desc, key, colourId);
            // update alarms in Firestore
            for (int i = 0; i < remLayout.getChildCount(); i++) {
                // TODO
                // alarmId
                CheckBox child = (CheckBox) remLayout.getChildAt(i);
                Intent alarmIntent = new Intent(EditTaskAct.this, AlarmReceiver.class);
                alarmIntent.putExtra("key", key);
                alarmIntent.putExtra("taskName", name);
                alarmIntent.putExtra("alarmType", i);

                long alarmTime = updatedTask.getAlarmTime(i);
                alarmIntent.putExtra("alarmTime", alarmTime);

                DocumentReference ref = remRef.document(String.valueOf(i));
                ref.get().addOnSuccessListener(documentSnapshot -> {
                    Integer requestCode = null;
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        requestCode = documentSnapshot.get("requestCode", Integer.class);
                        alarmIntent.putExtra("requestCode", requestCode);
                    }

                    if (child.isChecked()) {
                        new AlarmReceiver().setAlarm(EditTaskAct.this, alarmIntent);
                    } else if (requestCode != null) {
                        new AlarmReceiver().cancelAlarmIfExists(EditTaskAct.this,
                                requestCode, ref, alarmIntent);
                    }
                });
            }

            // update tasks in Firestore
            MainActivity.userDoc.collection("Tasks").document(key)
                    .update("taskName", name,
                            "taskDeadline", deadline,
                            "taskDesc", desc,
                            "colourId", colourId)
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Save Failed", Toast.LENGTH_SHORT).show());
            MainActivity.backToMain(EditTaskAct.this);
        });

        // Cancel Button
        Button cancelBtn = findViewById(R.id.cancel_edit_button);
        cancelBtn.setOnClickListener(v -> MainActivity.backToMain(EditTaskAct.this));
    }

    public void setColourId(int colourId) {
        this.colourId = colourId;
    }

    public void setDeadline(LocalDateTime updated) {
        this.deadline = updated.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.editDeadline.setText(Task.getDeadlineString(deadline));
        this.prevDate = updated;
    }
}