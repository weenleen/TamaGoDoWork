package com.example.tamagodowork.bottomNav.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tamagodowork.R;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
        Day_Number.setText(String.valueOf(day));

        return view;
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
