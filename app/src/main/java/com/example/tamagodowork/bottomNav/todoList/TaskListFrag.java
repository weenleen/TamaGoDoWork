package com.example.tamagodowork.bottomNav.todoList;

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

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class TaskListFrag extends Fragment {

    private TaskAdapter adapter;
    private ArrayList<Task> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.frag_task_list, container, false);
        RecyclerView taskListView = view.findViewById(R.id.taskListView);
        taskListView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        list = new ArrayList<>();
        adapter = new TaskAdapter(getActivity(), this.list, TaskAdapter.AdapterType.TASK_LIST);
        taskListView.setAdapter(adapter);

        // Read data from Firestore
        MainActivity.userDoc.collection("Tasks")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // error
                        Toast.makeText(view.getContext(), "No Data", Toast.LENGTH_SHORT).show();
                    }

                    list.clear();

                    if (value == null) return;
                    for (QueryDocumentSnapshot doc : value) {
                        Long tmp = doc.get("taskDeadline", Long.class);
                        if (tmp == null) continue;
                        list.add(new Task(doc.getString("taskName"),
                                tmp,
                                doc.getString("taskDesc"),
                                doc.getId(),
                                doc.get("colourId", Integer.class)));
                    }

                    // might want to change to SortedList for more efficiency
                    Collections.sort(list);

                    adapter.notifyDataSetChanged();
                });

        return view;
    }
}
