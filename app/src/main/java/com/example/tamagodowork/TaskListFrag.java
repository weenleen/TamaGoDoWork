package com.example.tamagodowork;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class TaskListFrag extends Fragment {

    RecyclerView taskListView;
    TaskAdapter adapter;
    ArrayList<Task> list;

    FloatingActionButton fab;

    // firestore
    FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.task_list_frag, container, false);
        this.taskListView = view.findViewById(R.id.taskListView);
        this.taskListView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        /**
         * Retrieving data from firebase
         */
        list = new ArrayList<>();
        //reference = FirebaseDatabase.getInstance().getReference().child("TamaGoDoWork");
        adapter = new TaskAdapter(getActivity(), this.list);
        taskListView.setAdapter(adapter);

        // TODO
        db = FirebaseFirestore.getInstance();
        db.collection("Tasks").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    list.clear();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(new Task(document.getString("taskName"),
                                document.getString("taskDeadline"),
                                document.getString("taskDesc"),
                                document.getId()));
                    }

                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(view.getContext(), "No Data", Toast.LENGTH_SHORT).show();
                }
            }
        });





//        reference.addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                list.clear();
//
//                // set code to retrieve data and replace layout
//                for(DataSnapshot dataSnapshot: snapshot.getChildren())
//                {
//                    Task t = dataSnapshot.getValue(Task.class);
//                    list.add(t);
//                }
//
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // set code to show an error
//                Toast.makeText(view.getContext(), "No Data", Toast.LENGTH_SHORT).show();
//            }
//        });

        /**
         * Floating Action Button to add new tasks
         */
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddTaskAct.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
