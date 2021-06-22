package com.example.tamagodowork.bottomNav.todoList;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.tamagodowork.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Typeface.BOLD;

public class GridAdapter extends BaseAdapter {

    private final List<Date> dates;
    private final Calendar currentDate;
    private HashMap<Integer, ArrayList<Task>> monthTaskMap;
    private final Context context;

    public GridAdapter(@NonNull @NotNull Context context, List<Date> dates, Calendar currentDate,
                       HashMap<Integer, ArrayList<Task>> monthTaskMap) {
        this.context = context;
        this.dates = dates;
        this.currentDate = currentDate;
        this.monthTaskMap = monthTaskMap;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // date of the current grid item
        Date monthDate = this.dates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);

        int displayDay = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH) + 1;
        int displayYear = dateCalendar.get(Calendar.YEAR);



        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            view = inflater.inflate(R.layout.single_cell_layout, parent, false);
        }



        // number on the grid item
        TextView Day_Number = view.findViewById(R.id.calendar_day);
        Day_Number.setText(String.valueOf(displayDay));


        // highlight current date
        Calendar currentData = Calendar.getInstance(); // today's date
        if (displayDay == currentData.get(Calendar.DAY_OF_MONTH) && displayMonth == currentData.get(Calendar.MONTH) + 1 && displayYear == currentData.get(Calendar.YEAR)) {
            Day_Number.setBackground(AppCompatResources.getDrawable(context, R.drawable.bg_date_highlight));
            Day_Number.setTextColor(Color.WHITE);
        }



        // month and year on the calendar page
        int currentMonth = this.currentDate.get(Calendar.MONTH) + 1;
        int currentYear = this.currentDate.get(Calendar.YEAR);

        // checks if the current grid item belongs to the current month on the calendar
        if (displayMonth != currentMonth || displayYear != currentYear) { // other months
            return view; // no need to add events
        } else { // current month
            Day_Number.setTextColor(Color.BLACK);
            Day_Number.setTypeface(Day_Number.getTypeface(),BOLD);
        }



        // retrieve tasks for this day
        ArrayList<Task> dayTaskList = this.monthTaskMap.get(displayDay);
        int numOfTasks;

        if (dayTaskList == null) return view;
        else numOfTasks = dayTaskList.size();

        LinearLayout indicatorLayout = view.findViewById(R.id.schedule_task_indicator);

        // for every task for this day
        for (int i = 0; i < numOfTasks; i++) {
            if (i >= 5) break; // maximum 4 indicators and a plus symbol

            ImageView indicator = (ImageView) indicatorLayout.getChildAt(i);
            LinearLayout.LayoutParams childLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f / numOfTasks);
            indicator.setLayoutParams(childLayoutParams);
        }

        return view;
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

    @Override
    public long getItemId(int position) {
        return position;
    }

}