package com.example.tamagodowork;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.api.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ScheduleFrag extends Fragment {

    ImageButton NextButton, PreviousButton;
    TextView CurrentDate;
    GridView gridView;
    View view;
    DBOpenHelper dbOpenHelper;
    private static final int MAX_CALENDAR_DAY = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);

    GridAdapter gridAdapter;
    AlertDialog alertDialog;
    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.schedule_frag, container, false);


        NextButton = view.findViewById(R.id.month_navigation_next);
        PreviousButton = view.findViewById(R.id.month_navigation_previous);
        CurrentDate = view.findViewById(R.id.currentDate);
        gridView = view.findViewById(R.id.gridView);


        PreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                SetUpCalendar();
            }
        });

        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                SetUpCalendar();
            }
        });

        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View addView = inflater.inflate(R.layout.add_new_event, null);
                builder.setView(addView);

                TextView EventTime = addView.findViewById(R.id.eventTime);
                EditText EventName = addView.findViewById(R.id.events_id);
                Button SetTime = addView.findViewById(R.id.setTime);
                Button AddEvent = addView.findViewById(R.id.add_new_evt_button);
                Button CancelEvent = addView.findViewById(R.id.cancel_button);


                SetTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), R.style.Theme_AppCompat_Dialog,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        Calendar c = Calendar.getInstance();
                                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        c.set(Calendar.MINUTE, minute);
                                        c.setTimeZone(TimeZone.getDefault());
                                        SimpleDateFormat hourFormat = new SimpleDateFormat("K:mm a", Locale.ENGLISH);
                                        String event_Time = hourFormat.format(c.getTime());
                                        EventTime.setText(event_Time);


                                    }
                                }, hours, minute, false);

                        timePickerDialog.show();


                    }
                });

                //create alert dialog
                AlertDialog alertDialog = builder.create();
                //show alert box
                alertDialog.show();





                String date = dateFormat.format(dates.get(position));
                String month = monthFormat.format(dates.get(position));
                String year = yearFormat.format(dates.get(position));

                /*
                AddEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveEvent(EventName.getText().toString(),EventTime.getText().toString(),date, month, year);
                        SetUpCalendar();
                        alertDialog.dismiss();

                    }
                });
                */

                CancelEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }

        });

        return view;

    }

    private void SaveEvent(String event, String time, String date, String month,  String year) {
        dbOpenHelper = new DBOpenHelper(getContext());
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.saveEvent(event, time, date, month, year, database);
        dbOpenHelper.close();
        Toast.makeText(getContext(), "Event Saved", Toast.LENGTH_LONG);
    }

    private void SetUpCalendar() {
        String currentDate = dateFormat.format(calendar.getTime());
        CurrentDate.setText(currentDate);
        dates.clear();
        // clones a month calendar
        Calendar monthCalendar = (Calendar) calendar.clone();
        // sets the values for the calendar fields, YEAR, MONTH, DAY_OF_MONTH, HOUR_OF_DAY, MINUTE & SECOND
        monthCalendar.set(Calendar.DAY_OF_MONTH,1);
        int firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        // adds or subtracts the specified amount of time to the given calendar field, based on the calendar rules
        // 2nd param : amount of time to be added to this field
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);

        while (dates.size() < MAX_CALENDAR_DAY) {
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        gridAdapter = new GridAdapter(getContext(), dates, calendar, eventsList);
        gridView.setAdapter(gridAdapter);
    }
}
