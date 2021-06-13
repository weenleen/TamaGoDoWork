package com.example.tamagodowork.bottomNav.schedule;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tamagodowork.R;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class GridAdapter extends ArrayAdapter {
    List<Date> dates;
    Calendar currentDate;
    List<Events> events;
    LayoutInflater inflater;

    public GridAdapter(@NonNull @NotNull Context context, List<Date> dates, Calendar currentDate, List<Events> events) {
        super(context, R.layout.single_cell_layout);

        this.dates = dates;
        this.currentDate = currentDate;
        this.events = events;
        inflater = LayoutInflater.from(getContext());

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date monthDate = dates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);
        int day = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH) + 1;
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.single_cell_layout, parent, false);
        }
        if (displayMonth == currentMonth && displayYear == currentYear) {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.light_pink));
        }
        else {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.off_white));
        }

        TextView Day_Number = view.findViewById(R.id.calendar_day);
        TextView eventsPerDay = view.findViewById(R.id.noOfEvents);
        Day_Number.setText(String.valueOf(day));
        /*
        Calendar eventCalendar = Calendar.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();
        // events size == 4
        Log.d(TAG, String.valueOf(events.size()));
        for (int i = 0; i < events.size(); i++) {
            Log.d(TAG, "DATE:" + events.get(i).getDATE());
            eventCalendar.setTime(convertStringToDate(events.get(i).getDATE()));
            if (day == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalendar.get(Calendar.MONTH)) {
                arrayList.add(events.get(i).getEVENT());
                eventsPerDay.setText(String.valueOf(arrayList.size()));
            }

        }
        */


        return view;
    }

    private Date convertStringToDate(String eventDate) {
        SimpleDateFormat format = new SimpleDateFormat("MM-yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(eventDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    // returns index of item
    @Override
    public int getPosition(@Nullable Object item) {
        return dates.indexOf(item);
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }
}
