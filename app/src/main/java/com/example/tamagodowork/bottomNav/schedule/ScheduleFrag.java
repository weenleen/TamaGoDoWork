package com.example.tamagodowork.bottomNav.schedule;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class ScheduleFrag extends Fragment {

    ImageButton NextButton, PreviousButton;
    TextView CurrentDate;

    // initialize views
    GridView gridView;
    RecyclerView eventListView;
    View view;
    FirebaseFirestore db;
    private static final int MAX_CALENDAR_DAY = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    // initialize adapters
    GridAdapter gridAdapter;
    EventsRecyclerAdapter eventAdapter;

    //initialize others
    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();

    private Context context;

    // initialization of all layout upon start
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

        // grid
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.schedule_frag, container, false);

        gridView = view.findViewById(R.id.gridView);
        gridView.setAdapter(gridAdapter);



        // next month button
        NextButton = view.findViewById(R.id.month_navigation_next);
        NextButton.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, 1);
            SetUpCalendar();
        });


        // previous month button
        PreviousButton = view.findViewById(R.id.month_navigation_previous);
        PreviousButton.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, -1);
            SetUpCalendar();
        });


        //
        CurrentDate = view.findViewById(R.id.currentDate);
        CurrentDate.setOnClickListener(v -> {
        });



        // for every item in the grid
        gridView = view.findViewById(R.id.gridView);
        gridView.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            // TODO Auto-generated method stub
            String date = eventDateFormat.format(dates.get(position));
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            LayoutInflater inflater1 = requireActivity().getLayoutInflater();
            View addView = inflater1.inflate(R.layout.add_new_event, null);
            builder.setView(addView);
            //create alert dialog
            AlertDialog alertDialog = builder.create();
            //show alert box
            alertDialog.show();


            EditText SetName = addView.findViewById(R.id.setEvent);
            EditText SetTime = addView.findViewById(R.id.setTime);
            SetTime.setFocusable(false);
            SetTime.setCursorVisible(false);
            EditText Duration = addView.findViewById(R.id.duration);
            //EditText SetEndTime = addView.findViewById(R.id.setEndTime);
            Button AddEvent = addView.findViewById(R.id.add_new_evt_button);
            Button CancelEvent = addView.findViewById(R.id.cancel_button);


            SetTime.setOnClickListener(v -> {
                Calendar calendar = Calendar.getInstance();
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(),
                        (view, hourOfDay, minute1) -> {
//                            Calendar c = Calendar.getInstance();
//                            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                            c.set(Calendar.MINUTE, minute1);
//                            c.setTimeZone(TimeZone.getDefault());
//                            SimpleDateFormat hourFormat = new SimpleDateFormat("K:mm a", Locale.ENGLISH);
//                            String event_Time = hourFormat.format(c.getTime());

                            String event_Time = hourOfDay + ":" + minute1;
                            SetTime.setText(event_Time);

                        }, hours, minute, false);

                timePickerDialog.show();
            });



            AddEvent.setOnClickListener(v -> {
                String event = SetName.getText().toString();
                String eventTime = SetTime.getText().toString();
                int duration = Integer.parseInt(Duration.getText().toString());
                String date1 = eventDateFormat.format(dates.get(position));
                String endDate = eventDateFormat.format(dates.get(position+duration));
                String month = monthFormat.format(dates.get(position));
                String year = yearFormat.format(dates.get(position));
                DocumentReference ref = MainActivity.userDoc.collection("Events").document();
                String key = ref.getId();

                if (TextUtils.isEmpty(event)) {
                    SetName.setError("Please enter a name");
                    return;
                }
                if (TextUtils.isEmpty(eventTime)) {
                    SetTime.setError("Please enter a name");
                    return;
                }
                if (TextUtils.isEmpty(String.valueOf(duration))) {
                    Duration.setError("Please enter a duration");
                    return;
                }
                // Put Event in Firestore
                Events addedEvent = new Events(event, eventTime, date1, endDate, month, year, key);
                ref.set(addedEvent);
                SetUpCalendar();

                RecyclerView eventsView = view.findViewById(R.id.recyclerView);
                ArrayList<Events> list = new ArrayList<>();
                eventsView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                EventsRecyclerAdapter adapter = new EventsRecyclerAdapter(context, list);
                eventsView.setAdapter(adapter);
                db = FirebaseFirestore.getInstance();
                MainActivity.userDoc.collection("Events").get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    if (date1.equals(document.getString("startdate"))) {
                                        list.add(new Events(document.getString("event"), document.getString("time"), document.getString("startdate"),
                                                document.getString("enddate"), document.getString("month"), document.getString("year"), document.getId()));
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        });

                alertDialog.dismiss();
            });






            CancelEvent.setOnClickListener(v -> alertDialog.dismiss());

            RecyclerView eventsView = view.findViewById(R.id.recyclerView);
            ArrayList<Events> list = new ArrayList<>();
            eventsView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            EventsRecyclerAdapter adapter = new EventsRecyclerAdapter(context, list);
            eventsView.setAdapter(adapter);
            db = FirebaseFirestore.getInstance();
            MainActivity.userDoc.collection("Events").get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (date.equals(document.getString("startdate"))) {
                                    list.add(new Events(document.getString("event"), document.getString("time"), document.getString("startdate"),
                                            document.getString("enddate"), document.getString("month"), document.getString("year"), document.getId()));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        });

        return view;

    }


    private ArrayList<Events> collectEventsByDay(String date) {
        ArrayList<Events> emptyList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        MainActivity.userDoc.collection("Events").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            if (date.equals(document.getString("startdate"))) {
                                emptyList.add(new Events(document.getString("event"), document.getString("time"), document.getString("startdate"),
                                        document.getString("enddate"), document.getString("month"), document.getString("year"), document.getId()));
                            }
                        }

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        return emptyList;


    }

    private void collectEventsPerMonth(String MONTH, String YEAR) {

        eventsList.clear();
        db = FirebaseFirestore.getInstance();
        MainActivity.userDoc.collection("Events").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {


                            if (MONTH.equals(document.getString("month")) && YEAR.equals(document.getString("year"))) {
                                eventsList.add(new Events(document.getString("event"), document.getString("time"), document.getString("startdate"),
                                        document.getString("enddate"),document.getString("month"), document.getString("year"), document.getId()));
                            }


                        }
                        gridAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

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
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);
        collectEventsPerMonth(monthFormat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));


        while (dates.size() < MAX_CALENDAR_DAY) {
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        gridAdapter = new GridAdapter(context, dates, calendar, eventsList);
        gridView.setAdapter(gridAdapter);
    }
}
