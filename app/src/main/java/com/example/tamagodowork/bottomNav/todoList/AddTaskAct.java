package com.example.tamagodowork.bottomNav.todoList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.example.tamagodowork.alarm.AlarmReceiver;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentReference;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class AddTaskAct extends AppCompatActivity {

    private Context context;

    private EditText addName, addDeadline, addDesc;
    private ImageButton addColour;
    private long deadline;

    private static Integer colourId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        this.context = getApplicationContext();

        this.addName = findViewById(R.id.addName);
        this.addDesc = findViewById(R.id.addDesc);

        this.addDeadline = findViewById(R.id.addDeadline);
        this.addDeadline.setOnClickListener(v -> {
            final View dialogView = View.inflate(context, R.layout.date_time_picker, null);
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


        // add colour
        this.addColour = findViewById(R.id.addColour);
        ((GradientDrawable) addColour.getDrawable()).setColor(
                ContextCompat.getColor(context, Task.colours[0]));
        this.addColour.setOnClickListener(v -> {
            final View dialogView = View.inflate(context, R.layout.dial_colour_picker, null);
            final BottomSheetDialog dialog = new BottomSheetDialog(AddTaskAct.this);

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
                    colourId = c;
                    ((GradientDrawable) addColour.getDrawable()).setColor(
                            ContextCompat.getColor(context, c));
                    dialog.dismiss();
                });
            }

            dialog.setContentView(dialogView);
            dialog.show();
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
            Task addedTask = new Task(name, deadline, desc, key, colourId);
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