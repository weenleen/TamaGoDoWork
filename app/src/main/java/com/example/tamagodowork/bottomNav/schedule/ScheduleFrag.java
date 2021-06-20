package com.example.tamagodowork.bottomNav.schedule;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.example.tamagodowork.bottomNav.taskList.TaskAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Context;
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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

    // initialization of all layout upon start
    @Override
    public void onStart() {
        super.onStart();
        SetUpCalendar();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // grid
        inflater = (LayoutInflater) getActivity().getSystemService(this.getActivity().LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.schedule_frag, container, false);

        gridView = view.findViewById(R.id.gridView);
        gridView.setAdapter(gridAdapter);




        NextButton = view.findViewById(R.id.month_navigation_next);
        PreviousButton = view.findViewById(R.id.month_navigation_previous);
        CurrentDate = view.findViewById(R.id.currentDate);
        gridView = view.findViewById(R.id.gridView);





        PreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                SetUpCalendar();
            }
        });

        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                SetUpCalendar();
            }
        });

        CurrentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                String date = eventDateFormat.format(dates.get(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View addView = inflater.inflate(R.layout.add_new_event, null);
                builder.setView(addView);
                //create alert dialog
                AlertDialog alertDialog = builder.create();
                //show alert box
                alertDialog.show();


                EditText SetName = addView.findViewById(R.id.setEvent);
                EditText SetTime = addView.findViewById(R.id.setTime);
                EditText Duration = addView.findViewById(R.id.duration);
                //EditText SetEndTime = addView.findViewById(R.id.setEndTime);
                Button AddEvent = addView.findViewById(R.id.add_new_evt_button);
                Button CancelEvent = addView.findViewById(R.id.cancel_button);


                SetTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), R.style.Theme_AppCompat_Dialog,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        Calendar c = Calendar.getInstance();
                                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        c.set(Calendar.MINUTE, minute);
                                        c.setTimeZone(TimeZone.getDefault());
                                        SimpleDateFormat hourFormat = new SimpleDateFormat("K:mm a", Locale.ENGLISH);
                                        String event_Time = hourFormat.format(c.getTime());
                                        SetTime.setText(event_Time);

                                    }
                                }, hours, minute, false);

                        timePickerDialog.show();
                    }
                });



                AddEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String event = SetName.getText().toString();
                        String eventTime = SetTime.getText().toString();
                        int duration = Integer.valueOf(Duration.getText().toString());
                        String date = eventDateFormat.format(dates.get(position));
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
                        Events addedEvent = new Events(event, eventTime, date, endDate, month, year, key);
                        ref.set(addedEvent);
                        SetUpCalendar();

                        RecyclerView eventsView = view.findViewById(R.id.recyclerView);
                        ArrayList<Events> list = new ArrayList<>();
                        eventsView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                        EventsRecyclerAdapter adapter = new EventsRecyclerAdapter(getActivity(), list);
                        eventsView.setAdapter(adapter);
                        db = FirebaseFirestore.getInstance();
                        db.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("Events").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.e(TAG, "doc date" + document.getString("startdate"));

                                                if (date.equals(document.getString("startdate"))) {
                                                    list.add(new Events(document.getString("event"), document.getString("time"), document.getString("startdate"),
                                                            document.getString("enddate"), document.getString("month"), document.getString("year"), document.getId()));
                                                }
                                            }
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });

                        MainActivity.userDoc.update("selectedFrag", MainActivity.SCHEDULE_FRAG);
                        alertDialog.dismiss();
                    }
                });






                CancelEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                RecyclerView eventsView = view.findViewById(R.id.recyclerView);
                ArrayList<Events> list = new ArrayList<>();
                eventsView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                EventsRecyclerAdapter adapter = new EventsRecyclerAdapter(getActivity(), list);
                eventsView.setAdapter(adapter);
                db = FirebaseFirestore.getInstance();
                db.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("Events").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
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
                            }
                        });

                MainActivity.userDoc.update("selectedFrag", MainActivity.SCHEDULE_FRAG);


            }

        });

        return view;

    }


    private ArrayList<Events> collectEventsByDay(String date) {
        ArrayList<Events> emptyList = new ArrayList<Events>();
        db = FirebaseFirestore.getInstance();
        db.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("Events").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if (date.equals(document.getString("startdate"))) {
                                    emptyList.add(new Events(document.getString("event"), document.getString("time"), document.getString("startdate"),
                                            document.getString("enddate"), document.getString("month"), document.getString("year"), document.getId()));
                                }
                            }
                            
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return emptyList;


    }

    private void collectEventsPerMonth(String MONTH, String YEAR) {

        eventsList.clear();
        db = FirebaseFirestore.getInstance();
        db.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("Events").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
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

        gridAdapter = new GridAdapter(getContext(), dates, calendar, eventsList);
        gridView.setAdapter(gridAdapter);
    }
}
