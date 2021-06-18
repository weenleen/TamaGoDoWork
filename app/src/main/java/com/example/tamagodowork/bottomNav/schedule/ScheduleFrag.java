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
    GridView gridView;
    View view;
    FirebaseFirestore db;
    private static final int MAX_CALENDAR_DAY = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    GridAdapter gridAdapter;
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
                        String date = eventDateFormat.format(dates.get(position));
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

                        // Put Event in Firestore
                        Events addedEvent = new Events(event, eventTime, date, month, year);
                        ref.set(addedEvent);
                        // SaveEvent(EventName.getText().toString(),EventTime.getText().toString(),date, month, year);
                        SetUpCalendar();
                        alertDialog.dismiss();
                    }
                });






                CancelEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });



            }

        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String date = eventDateFormat.format(dates.get(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View addView = inflater.inflate(R.layout.display_event,null);



                TextView eventName = addView.findViewById(R.id.eventName);
                TextView eventDate = addView.findViewById(R.id.eventDate);
                TextView eventTime = addView.findViewById(R.id.eventTime);
                RecyclerView eventListView = addView.findViewById(R.id.events_view);

                /*
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(addView.getContext());
                eventListView.setLayoutManager(layoutManager);
                eventListView.setHasFixedSize(true);
                EventsRecyclerAdapter listAdapter = new EventsRecyclerAdapter(addView.getContext(),collectEventsByDay(date));
                eventListView.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();
                */


                builder.setView(addView);
                AlertDialog alertDialog = builder.create();
                //show alert box
                alertDialog.show();


                return true;
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

                                if (date.equals(document.getString("date"))) {
                                    emptyList.add(new Events(document.getString("event"), document.getString("time"), document.getString("date"), document.getString("month"), document.getString("year")));
                                }
                            }
                            //listAdapter.notifyDataSetChanged();
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

                                Log.d(TAG, "month: " + MONTH);
                                Log.d(TAG, "doc_month: " + document.getString("month"));
                                Log.d(TAG, "same month: " +  String.valueOf(MONTH == document.getString("month")));
                                Log.d(TAG, "same year" + String.valueOf(MONTH == document.getString("month")));
                                Log.d(TAG, "doc_year: " + document.getString("year"));
                                Log.d(TAG, "year: " + YEAR);
                                Log.d(TAG, "bob");
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Log.d(TAG, "date" + document.getString("date"));

                                if (MONTH.equals(document.getString("month")) && YEAR.equals(document.getString("year"))) {
                                    Log.d(TAG, "month: " + MONTH);
                                    Log.d(TAG, "year: " + YEAR);
                                    Log.d(TAG, "bob");
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    Log.d(TAG, "date" + document.getString("date"));
                                    eventsList.add(new Events(document.getString("event"), document.getString("time"), document.getString("date"), document.getString("month"), document.getString("year")));
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
