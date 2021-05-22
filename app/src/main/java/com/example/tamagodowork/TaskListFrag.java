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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TaskListFrag extends Fragment {

    RecyclerView taskListView;
    DatabaseReference reference;
    TaskAdapter adapter;
    ArrayList<Task> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View view =  inflater.inflate(R.layout.task_list_frag, container, false);
        this.taskListView = view.findViewById(R.id.taskListView);
        this.taskListView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // get data from firebase
        list = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("TamaGoDoWork");
        adapter = new TaskAdapter(getActivity(), this.list);
        taskListView.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // set code to retrieve data and replace layout
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    Task t = dataSnapshot.getValue(Task.class);
                    list.add(t);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // set code to show an error
                Toast.makeText(view.getContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });

        // Floating Action Button to add new tasks
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getParentFragmentManager() , AddNewTask.TAG);
            }
        });

        return view;
    }



}
