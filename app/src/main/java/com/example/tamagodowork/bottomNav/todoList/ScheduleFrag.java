package com.example.tamagodowork.bottomNav.todoList;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    ImageButton NextButton, PreviousButton;
    TextView CurrentDate;

    // initialize views
    private static final int MAX_CALENDAR_DAY = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    // initialize adapters
    GridView gridView;
    GridAdapter gridAdapter;
    RecyclerView recyclerView;
    TaskAdapter recyclerAdapter;

    //initialize others
    ArrayList<Date> dates = new ArrayList<>();

    private HashMap<Integer, ArrayList<Task>> monthTaskMap = new HashMap<>();

    private Context context;

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
        View view = inflater.inflate(R.layout.schedule_frag, container, false);


        // grid
        this.gridView = view.findViewById(R.id.gridView);


        // recycler view
        this.recyclerView = view.findViewById(R.id.schedule_recyclerView);
        this.recyclerAdapter = new TaskAdapter(context, new ArrayList<Task>());
        this.recyclerView.setAdapter(this.recyclerAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(context));


        // prev button
        PreviousButton = view.findViewById(R.id.month_navigation_previous);
        PreviousButton.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, -1);
            SetUpCalendar();
        });


        // next button
        NextButton = view.findViewById(R.id.month_navigation_next);
        NextButton.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, 1);
            SetUpCalendar();
        });


        // current month date textView
        CurrentDate = view.findViewById(R.id.currentDate);


        // listener for every grid item
        gridView.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            // TODO Auto-generated method stub

            Date selectedDate = dates.get(position);
            int day = selectedDate.toInstant().atZone(ZoneId.systemDefault()).getDayOfMonth();

            ArrayList<Task> dayTaskList = monthTaskMap.get(day);

            if (dayTaskList == null) {
                dayTaskList = new ArrayList<>();
            }

            recyclerAdapter = new TaskAdapter(context, dayTaskList);
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

                    monthTaskMap.clear();

                    assert value != null;
                    for (QueryDocumentSnapshot doc : value) {
                        Long tmp = doc.get("taskDeadline", Long.class);
                        if (tmp == null) {
                            continue;
                        }

                        ZonedDateTime dateTime = Instant.ofEpochMilli(tmp).atZone(ZoneId.systemDefault());

                        if (month != dateTime.getMonthValue() || year != dateTime.getYear()) {
                            Log.e("task month", String.valueOf(dateTime.getMonthValue()));
                            Log.e("task year", String.valueOf(dateTime.getYear()));
                            continue;
                        }

                        int dayOfMonth = dateTime.getDayOfMonth();
                        ArrayList<Task> dayTaskList = monthTaskMap.get(dayOfMonth);

                        if (dayTaskList == null) {
                            dayTaskList = new ArrayList<>();
                            monthTaskMap.put(dayOfMonth, dayTaskList);
                        }

                        Log.e("task added", String.valueOf(dayOfMonth));
                        dayTaskList.add(new Task(doc.getString("taskName"),
                                tmp,
                                doc.getString("taskDesc"),
                                doc.getId()));
                    }

                    Log.e("monthTaskMap size", String.valueOf(monthTaskMap.size()));
                    gridAdapter.notifyDataSetChanged();
                });
    }

    /**
     *
     */
    private void SetUpCalendar() {
        String currentDate = dateFormat.format(calendar.getTime());
        CurrentDate.setText(currentDate);
        dates.clear();

        monthTaskMap = new HashMap<>();
        this.gridAdapter = new GridAdapter(context, dates, calendar, monthTaskMap);
        this.gridView.setAdapter(gridAdapter);

        // clones a month calendar
        Calendar monthCalendar = (Calendar) calendar.clone();
        // sets the values for the calendar fields, YEAR, MONTH, DAY_OF_MONTH, HOUR_OF_DAY, MINUTE & SECOND
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);

        Log.e("collect month", String.valueOf(monthCalendar.get(Calendar.MONTH)));
        Log.e("collect year", String.valueOf(calendar.get(Calendar.YEAR)));
        collectTasksPerMonth(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));


        while (dates.size() < MAX_CALENDAR_DAY) {
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
}