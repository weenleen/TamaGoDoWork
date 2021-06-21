package com.example.tamagodowork.bottomNav.todoList;

import android.content.Intent;
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

import com.example.tamagodowork.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class TaskListFrag extends Fragment {

    RecyclerView taskListView;
    TaskAdapter adapter;
    ArrayList<Task> list;

    FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.task_list_frag, container, false);
        this.taskListView = view.findViewById(R.id.taskListView);
        this.taskListView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        list = new ArrayList<>();
        adapter = new TaskAdapter(getActivity(), this.list);
        taskListView.setAdapter(adapter);

        // get user info
        String userID = "";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) userID = user.getUid();

        // Read data from Firestore
        db = FirebaseFirestore.getInstance();
        db.collection("Users").document(userID).collection("Tasks")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // error
                        Toast.makeText(view.getContext(), "No Data", Toast.LENGTH_SHORT).show();
                    }

                    list.clear();

                    assert value != null;
                    for (QueryDocumentSnapshot doc : value) {
                        Long tmp = doc.get("taskDeadline", Long.class);
                        if (tmp == null) continue;
                        list.add(new Task(doc.getString("taskName"),
                                tmp,
                                doc.getString("taskDesc"),
                                doc.getId()));
                    }

                    // might want to change to SortedList for more efficiency
                    Collections.sort(list);

                    adapter.notifyDataSetChanged();
                });

        return view;
    }
}
