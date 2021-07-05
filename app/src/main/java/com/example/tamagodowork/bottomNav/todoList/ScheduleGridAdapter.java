package com.example.tamagodowork.bottomNav.todoList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import androidx.core.content.ContextCompat;

import com.example.tamagodowork.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.graphics.Typeface.BOLD;

public class ScheduleGridAdapter extends BaseAdapter {

    private final List<Date> dates;
    private final Calendar currentDate;
    private final HashMap<Integer, ArrayList<Todo>> monthTodoMap;
    private final Context context;

    public ScheduleGridAdapter(@NonNull @NotNull Context context, List<Date> dates, Calendar currentDate,
                       HashMap<Integer, ArrayList<Todo>> monthTodoMap) {
        this.context = context;
        this.dates = dates;
        this.currentDate = currentDate;
        this.monthTodoMap = monthTodoMap;
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
            view = inflater.inflate(R.layout.schedule_grid_item, parent, false);
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



        // retrieve todos for this day
        ArrayList<Todo> dayTodoList = this.monthTodoMap.get(displayDay);
        int numOfTodos;

        if (dayTodoList == null) return view;
        else numOfTodos = dayTodoList.size();

        LinearLayout indicatorLayout = view.findViewById(R.id.schedule_task_indicator);

        // all todos for this day
        for (int i = 0; i < numOfTodos; i++) {
            if (i >= 5) break; // maximum 4 indicators and a plus symbol


            ImageView indicator = (ImageView) indicatorLayout.getChildAt(i);
            LinearLayout.LayoutParams childLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f / numOfTodos);
            indicator.setLayoutParams(childLayoutParams);

            if (i >= 4) break; // below is for the circles and not plus sign

            GradientDrawable tmp = (GradientDrawable) indicator.getDrawable();

            if (tmp != null) {
                int color;
                try {
                    color = ContextCompat.getColor(context, dayTodoList.get(i).getColourId());
                } catch (Exception e) {
                    dayTodoList.get(i).setColourId(Todo.colours[0]);
                    color = ContextCompat.getColor(context, Todo.colours[0]);
                }

                tmp.setColor(color);
            }
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