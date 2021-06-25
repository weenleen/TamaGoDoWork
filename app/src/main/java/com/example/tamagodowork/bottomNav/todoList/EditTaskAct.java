package com.example.tamagodowork.bottomNav.todoList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;


public class EditTaskAct extends AppCompatActivity {

    private Context context;

    private EditText editName, editDeadline, editDesc;
    private ImageButton editColour;

    private String key;
    private long deadline;
    private int colourId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);

        this.context = getApplicationContext();

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
        LocalDateTime prevDate = task.getDateTime();
        this.editDeadline.setOnClickListener(v -> {
            final View dialogView = View.inflate(context, R.layout.dial_date_time_picker, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(EditTaskAct.this).create();

            DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
            TimePicker timePicker = dialogView.findViewById(R.id.time_picker);

            // make pickers display previous deadline
            datePicker.updateDate(prevDate.getYear(), prevDate.getMonthValue() - 1, prevDate.getDayOfMonth());
            timePicker.setCurrentHour(prevDate.getHour());
            timePicker.setCurrentMinute(prevDate.getMinute());

            // set date button
            Button setDateTimeBtn = dialogView.findViewById(R.id.date_time_set_btn);
            setDateTimeBtn.setOnClickListener(v1 -> {
                deadline = LocalDateTime.of(
                        datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(), timePicker.getCurrentMinute())
                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                editDeadline.setText(Task.getDeadlineString(deadline));
                alertDialog.dismiss();
            });

            alertDialog.setView(dialogView);
            alertDialog.show();
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

            // update alarms in Firestore
            for (int i = 0; i < remLayout.getChildCount(); i++) {
                // TODO
                // alarmId
                CheckBox child = (CheckBox) remLayout.getChildAt(i);
                if (child.isChecked()) {
                    remRef.document(String.valueOf(i)).set(
                            Map.of("alarmId",
                                    new Task("", deadline, "", "", null).getAlarmTime(i)));
                } else {
                    remRef.document(String.valueOf(i)).delete();
                }
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
}