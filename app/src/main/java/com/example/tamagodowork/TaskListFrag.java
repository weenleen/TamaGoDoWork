package com.example.tamagodowork;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TaskListFrag extends Fragment {

    RecyclerView taskListView;
    TaskAdapter adapter;
    DatabaseReference reference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ArrayList<Task> stuff = new ArrayList<>();

        View view =  inflater.inflate(R.layout.task_list_frag, container, false);
        this.taskListView = getView().findViewById(R.id.taskListView);

        // get data from firebase
        reference = FirebaseDatabase.getInstance().getReference().child("DoesApp");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // set code to retrieve data and replace layout
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Task t = dataSnapshot1.getValue(Task.class);
                    stuff.add(t);
                }
                adapter = new TaskAdapter(stuff);
                taskListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // set code to show an error
                Toast.makeText(view.getContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });

        this.taskListView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }
}
