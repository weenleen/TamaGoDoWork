package com.example.tamagodowork.bottomNav.todoList;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tamagodowork.R;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static android.graphics.Typeface.BOLD;

public class GridAdapter extends BaseAdapter {

    List<Date> dates;
    Calendar currentDate;
    HashMap<Integer, ArrayList<Task>> monthTaskMap;
    LayoutInflater inflater;
    Context context;

    public GridAdapter(@NonNull @NotNull Context context, List<Date> dates, Calendar currentDate,
                       HashMap<Integer, ArrayList<Task>> monthTaskMap) {
        this.context = context;
        this.dates = dates;
        this.currentDate = currentDate;
        this.monthTaskMap = monthTaskMap;
        inflater = LayoutInflater.from(context);
        Log.e("gridAdapter map", String.valueOf(monthTaskMap.size()));
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
            view.setBackgroundColor(context.getResources().getColor(R.color.light_pink));
        }
        else {
            view.setBackgroundColor(context.getResources().getColor(R.color.off_white));
        }

        TextView Day_Number = view.findViewById(R.id.calendar_day);
        Calendar currentData = Calendar.getInstance();

        // highlight current date
        if (day == currentData.get(Calendar.DAY_OF_MONTH) && displayMonth == currentData.get(Calendar.MONTH) + 1 && displayYear == currentData.get(Calendar.YEAR)) {
            view.setBackgroundColor(context.getResources().getColor(R.color.peach));
            Day_Number.setTextColor(Color.BLACK);
            Day_Number.setTypeface(Day_Number.getTypeface(),BOLD);
        }

        TextView eventsPerDay = view.findViewById(R.id.noOfEvents);
        Day_Number.setText(String.valueOf(day));


        ArrayList<Task> dayTaskList = monthTaskMap.get(day);
        int numOfTasks;

        if (dayTaskList == null) {
            numOfTasks = 0;
        } else {
            numOfTasks = dayTaskList.size();
        }



        if (numOfTasks == 0) {
            eventsPerDay.setText("");
        } else if (numOfTasks == 1) {
            eventsPerDay.setText(numOfTasks + " Event");
            Log.e("gridAdapter day", String.valueOf(day));
            Log.e("gridAdapter events", String.valueOf(numOfTasks));
        } else {
            eventsPerDay.setText(numOfTasks + " Event");
            Log.e("gridAdapter day", String.valueOf(day));
            Log.e("gridAdapter events", String.valueOf(numOfTasks));
        }

        return view;
    }

    private Date convertStringToDate(String eventDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
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

    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
}
