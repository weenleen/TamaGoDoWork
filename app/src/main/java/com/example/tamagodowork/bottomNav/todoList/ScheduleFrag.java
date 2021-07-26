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
import java.util.Collections;
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
    private ScheduleGridAdapter gridAdapter;
    private RecyclerView recyclerView;
    private TodoAdapter recyclerAdapter;

    // Data
    private final ArrayList<Date> dates = new ArrayList<>();
    private final HashMap<Integer, ArrayList<Todo>> monthTodoMap = new HashMap<>();

    private Context context;
    private final MainActivity main;
    private int gridSelectedPos = 0;


    public ScheduleFrag(@NonNull MainActivity main) {
        this.main = main;
    }

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

            ArrayList<Todo> dayTodoList = monthTodoMap.get(day);

            if (dayTodoList == null || month != calendar.get(Calendar.MONTH) + 1) {
                dayTodoList = new ArrayList<>();
            } else {
                Collections.sort(dayTodoList);
            }

            recyclerAdapter = new TodoAdapter(main, dayTodoList, TodoAdapter.AdapterType.SCHEDULE);
            recyclerView.setAdapter(recyclerAdapter);
        });


        return view;
    }


    /**
     *
     * @param month Month of the calendar.
     * @param year Year of the calendar.
     */
    private void collectTodosPerMonth(int month, int year) {
        main.userDoc.collection("Todos")
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;

                    this.monthTodoMap.clear();

                    assert value != null;
                    for (QueryDocumentSnapshot doc : value) {
                        Long tmp = doc.get("deadline", Long.class);
                        if (tmp == null) {
                            continue;
                        }

                        ZonedDateTime dateTime = Instant.ofEpochMilli(tmp).atZone(ZoneId.systemDefault());

                        if (month != dateTime.getMonthValue() || year != dateTime.getYear()) {
                            continue;
                        }

                        int dayOfMonth = dateTime.getDayOfMonth();
                        ArrayList<Todo> dayTodoList = this.monthTodoMap.get(dayOfMonth);

                        if (dayTodoList == null) {
                            dayTodoList = new ArrayList<>();
                            this.monthTodoMap.put(dayOfMonth, dayTodoList);
                        }

                        dayTodoList.add(doc.toObject(Todo.class));
                    }

                    this.recyclerAdapter.clear();
                    this.gridView.setAdapter(this.gridAdapter);
                    this.recyclerView.setAdapter(this.recyclerAdapter);
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
        this.monthTodoMap.clear();
        this.gridAdapter = new ScheduleGridAdapter(context, dates, calendar, monthTodoMap);
        this.gridView.setAdapter(gridAdapter);

        // clones a month calendar
        Calendar monthCalendar = (Calendar) calendar.clone();
        // sets the values for the calendar fields, YEAR, MONTH, DAY_OF_MONTH, HOUR_OF_DAY, MINUTE & SECOND
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);

        collectTodosPerMonth(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

        while (dates.size() < MAX_CALENDAR_DAY) {
            Date gridDate = monthCalendar.getTime();
            dates.add(gridDate);
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        this.recyclerAdapter = new TodoAdapter(main, new ArrayList<>(), TodoAdapter.AdapterType.SCHEDULE);
        this.recyclerView.setAdapter(recyclerAdapter);
    }
}