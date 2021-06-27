package com.example.tamagodowork.bottomNav.todoList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ScheduleFrag extends Fragment {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private static final int MAX_CALENDAR_DAY = 42;

    private final Calendar calendar = Calendar.getInstance(Locale.ENGLISH);

    // views
    private TextView monthTextView;
    private GridView gridView;
    private GridAdapter gridAdapter;
    private RecyclerView recyclerView;
    private TaskAdapter recyclerAdapter;

    // Data
    private final ArrayList<Date> dates = new ArrayList<>();
    private final HashMap<Integer, ArrayList<Task>> monthTaskMap = new HashMap<>();

    private Context context;
    private int gridSelectedPos = 0;

    @Override
    public void onStart() {
        super.onStart();
        SetUpCalendar();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.frag_schedule, container, false);


        // grid
        this.gridView = view.findViewById(R.id.gridView);


        // recycler view
        this.recyclerView = view.findViewById(R.id.schedule_recyclerView);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(context));


        // prev button
        ImageButton previousButton = view.findViewById(R.id.month_navigation_previous);
        previousButton.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, -1);
            SetUpCalendar();
        });


        // next button
        ImageButton nextButton = view.findViewById(R.id.month_navigation_next);
        nextButton.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, 1);
            SetUpCalendar();
        });


        // current month date textView
        monthTextView = view.findViewById(R.id.currentDate);


        // listener for every grid item
        gridView.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            // TODO Auto-generated method stub

            View prevView = gridView.getChildAt(gridSelectedPos);
            if (prevView != null) {
                RelativeLayout outline = prevView.findViewById(R.id.schedule_gridItem_bg);
                outline.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }


            View itemView = gridView.getChildAt(position);
            if (itemView != null) {
                RelativeLayout outline = itemView.findViewById(R.id.schedule_gridItem_bg);
                outline.setBackgroundColor(ContextCompat.getColor(context, R.color.peach));
            }

            gridSelectedPos = position;

            Date selectedDate = dates.get(position);
            int day = selectedDate.toInstant().atZone(ZoneId.systemDefault()).getDayOfMonth();
            int month = selectedDate.toInstant().atZone(ZoneId.systemDefault()).getMonthValue();

            ArrayList<Task> dayTaskList = monthTaskMap.get(day);

            if (dayTaskList == null || month != calendar.get(Calendar.MONTH) + 1) {
                dayTaskList = new ArrayList<>();
            }

            recyclerAdapter = new TaskAdapter(context, dayTaskList, TaskAdapter.AdapterType.SCHEDULE);
            recyclerView.setAdapter(recyclerAdapter);
        });


        return view;
    }


    /**
     *
     * @param month Month of the calendar.
     * @param year Year of the calendar.
     */
    private void collectTasksPerMonth(int month, int year) {
        MainActivity.userDoc.collection("Tasks")
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;

                    this.monthTaskMap.clear();

                    assert value != null;
                    for (QueryDocumentSnapshot doc : value) {
                        Long tmp = doc.get("taskDeadline", Long.class);
                        if (tmp == null) {
                            continue;
                        }

                        ZonedDateTime dateTime = Instant.ofEpochMilli(tmp).atZone(ZoneId.systemDefault());

                        if (month != dateTime.getMonthValue() || year != dateTime.getYear()) {
                            continue;
                        }

                        int dayOfMonth = dateTime.getDayOfMonth();
                        ArrayList<Task> dayTaskList = this.monthTaskMap.get(dayOfMonth);

                        if (dayTaskList == null) {
                            dayTaskList = new ArrayList<>();
                            this.monthTaskMap.put(dayOfMonth, dayTaskList);
                        }

                        dayTaskList.add(new Task(doc.getString("taskName"),
                                tmp,
                                doc.getString("taskDesc"),
                                doc.getId(),
                                doc.get("colourId", Integer.class)));
                    }

                    this.gridView.setAdapter(this.gridAdapter);
                });
    }

    /**
     * Sets up the grid view and calendar view.
     */
    private void SetUpCalendar() {
        // set the month text
        String monthStr = dateFormat.format(calendar.getTime());
        monthTextView.setText(monthStr);

        // set up data and adapters
        dates.clear();
        this.monthTaskMap.clear();
        this.gridAdapter = new GridAdapter(context, dates, calendar, monthTaskMap);
        this.gridView.setAdapter(gridAdapter);

        // clones a month calendar
        Calendar monthCalendar = (Calendar) calendar.clone();
        // sets the values for the calendar fields, YEAR, MONTH, DAY_OF_MONTH, HOUR_OF_DAY, MINUTE & SECOND
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);

        collectTasksPerMonth(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

        while (dates.size() < MAX_CALENDAR_DAY) {
            Date gridDate = monthCalendar.getTime();
            dates.add(gridDate);
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        this.recyclerAdapter = new TaskAdapter(context, new ArrayList<>(), TaskAdapter.AdapterType.SCHEDULE);
        this.recyclerView.setAdapter(recyclerAdapter);
    }
}