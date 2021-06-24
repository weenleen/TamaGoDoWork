package com.example.tamagodowork.bottomNav.todoList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

        this.key = getIntent().getStringExtra("key");
        final String deadlineStr = getIntent().getStringExtra("deadlineStr");

        // set the text in the views
        this.editName.setText(getIntent().getStringExtra("name"));
        this.editDeadline.setText(deadlineStr);
        this.editDesc.setText(getIntent().getStringExtra("desc"));

        // set deadline to previous deadline
        LocalDateTime prevDate = LocalDateTime.parse(deadlineStr,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm") );
        this.deadline = prevDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        // Check reminders Checkboxes
        LinearLayout remLayout = findViewById(R.id.reminders);
        CollectionReference remRef = MainActivity.userDoc.collection("Tasks")
                .document(key).collection("Reminders");
        remRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful() || task.getResult() == null) return;

            // alarmId
            for (QueryDocumentSnapshot doc : task.getResult()) {
                ((CheckBox) remLayout.getChildAt(Integer.parseInt(doc.getId()))).setChecked(true);
            }
        });


        // change deadline
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
        this.colourId = getIntent().getIntExtra("colourId", Task.colours[0]);
        this.editColour = findViewById(R.id.editColour);
        ((GradientDrawable) editColour.getDrawable()).setColor(
                ContextCompat.getColor(context, this.colourId));

        this.editColour.setOnClickListener(v -> {
            final View dialogView = View.inflate(context, R.layout.dial_colour_picker, null);
            final BottomSheetDialog dialog = new BottomSheetDialog(EditTaskAct.this);

            LinearLayout layout = dialogView.findViewById(R.id.colourPicker_image_layout);

            for (int i = 0; i < layout.getChildCount(); i++) {

                ImageView imageView = (ImageView) layout.getChildAt(i);
                GradientDrawable tmp = (GradientDrawable) AppCompatResources
                        .getDrawable(context, R.drawable.button_color_picker);

                int c = Task.colours[i];

                if (tmp != null) {
                    tmp.setColor(ContextCompat.getColor(context, c));
                    imageView.setImageDrawable(tmp);
                }

                imageView.setOnClickListener(v12 -> {
                    this.colourId = c;
                    ((GradientDrawable) editColour.getDrawable()).setColor(
                            ContextCompat.getColor(context, c));
                    dialog.dismiss();
                });
            }

            dialog.setContentView(dialogView);
            dialog.show();
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
}