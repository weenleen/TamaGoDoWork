package com.example.tamagodowork.bottomNav.todoList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.example.tamagodowork.R;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Calendar;

public class DialogDateTimePicker extends DialogFragment {

    public static final String TAG = "Date Time Picker Dialog";

    private final FragmentActivity activity;
    private LocalDateTime prevDate;

    private int updYear, updMonth, updDay;

    public DialogDateTimePicker(FragmentActivity activity, LocalDateTime prevDate) {
        this.activity = activity;
        this.prevDate = prevDate;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        final View dialogView = View.inflate(activity, R.layout.dial_date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

        CalendarView calendarView = dialogView.findViewById(R.id.date_picker);
        TimePicker timePicker = dialogView.findViewById(R.id.time_picker);

        // make pickers display previous deadline
        if (prevDate == null) {
            prevDate = LocalDateTime.now();
        }

        this.updYear = prevDate.getYear();
        this.updMonth = prevDate.getMonthValue();
        this.updDay = prevDate.getDayOfMonth();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, updYear);
        calendar.set(Calendar.MONTH, updMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, updDay);
        calendarView.setDate(calendar.getTimeInMillis(), true, true);
        timePicker.setCurrentHour(prevDate.getHour());
        timePicker.setCurrentMinute(prevDate.getMinute());

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            this.updYear = year;
            this.updMonth = month + 1;
            this.updDay = dayOfMonth;
        });


        // set date button
        Button setDateTimeBtn = dialogView.findViewById(R.id.date_time_set_btn);
        setDateTimeBtn.setOnClickListener(v1 -> {
            LocalDateTime updated = LocalDateTime.of(
                    this.updYear, this.updMonth, this.updDay,
                    timePicker.getCurrentHour(), timePicker.getCurrentMinute());
            if (activity instanceof AddTodoActivity) {
                ((AddTodoActivity) activity).setDeadline(updated);
            } else if (activity instanceof EditTodoActivity) {
                ((EditTodoActivity) activity).setDeadline(updated);
            }
            alertDialog.dismiss();
        });

        alertDialog.setView(dialogView);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return alertDialog;
    }
}
