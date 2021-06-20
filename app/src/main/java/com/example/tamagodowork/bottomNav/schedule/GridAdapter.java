package com.example.tamagodowork.bottomNav.schedule;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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

import static android.graphics.Typeface.BOLD;

public class GridAdapter extends BaseAdapter {

    Context context;
    List<Date> dates;
    Calendar currentDate;
    List<Events> events;
    LayoutInflater inflater;

    public GridAdapter(@NonNull @NotNull Context context, List<Date> dates, Calendar currentDate, List<Events> events) {
        this.context = context;
        this.dates = dates;
        this.currentDate = currentDate;
        this.events = events;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Date monthDate = dates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);
        Calendar eventCalendar = Calendar.getInstance();


        int day = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH) + 1;
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);


        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.single_cell_layout, parent, false);
        }

        if (displayMonth == currentMonth && displayYear == currentYear) {
            view.setBackgroundColor(context.getResources().getColor(R.color.light_pink));
        } else {
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

        Day_Number.setText(String.valueOf(day));

        LinearLayout stripeLayout = view.findViewById(R.id.event_stripe_layout);

        ArrayList<String> arrayList = new ArrayList<>();
        // events size == 4
        for (int i = 0; i < events.size(); i++) {
            eventCalendar.setTime(convertStringToDate(events.get(i).getSTARTDATE()));
            if (day == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalendar.get(Calendar.MONTH) + 1) {
                arrayList.add(events.get(i).getEVENT());
            }

            for (int s = 0; s < arrayList.size(); s++) {
                if (s > 3) break;

                TextView stripe = (TextView) stripeLayout.getChildAt(s);
                stripe.setBackgroundColor(context.getResources().getColor(R.color.peach));
            }
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
