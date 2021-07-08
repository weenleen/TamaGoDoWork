package com.example.tamagodowork.bottomNav.todoList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class TodoListFrag extends Fragment {

    private final TodoAdapter adapter;
    private final ArrayList<Todo> list;

    public TodoListFrag(MainActivity main) {
        this.list = new ArrayList<>();
        this.adapter = new TodoAdapter(main, this.list, TodoAdapter.AdapterType.TASK_LIST);

        // Read data from Firestore
        MainActivity.userDoc.collection("Todos")
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) return;

                    list.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        list.add(doc.toObject(Todo.class));
                    }

                    // might want to change to SortedList for more efficiency
                    Collections.sort(list);

                    adapter.notifyDataSetChanged();
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.frag_todo_list, container, false);
        RecyclerView taskListView = view.findViewById(R.id.taskListView);
        taskListView.setLayoutManager(new LinearLayoutManager(view.getContext()));

//        list = new ArrayList<>();
//        adapter = new TodoAdapter((MainActivity) getActivity(), this.list, TodoAdapter.AdapterType.TASK_LIST);
        taskListView.setAdapter(this.adapter);

        // Read data from Firestore
//        MainActivity.userDoc.collection("Todos")
//                .addSnapshotListener((value, error) -> {
//                    if (error != null) {
//                        // error
//                        Toast.makeText(view.getContext(), "No Data", Toast.LENGTH_SHORT).show();
//                    }
//
//                    list.clear();
//
//                    if (value == null) return;
//                    for (QueryDocumentSnapshot doc : value) {
//                        list.add(doc.toObject(Todo.class));
//                    }
//
//                    // might want to change to SortedList for more efficiency
//                    Collections.sort(list);
//
//                    adapter.notifyDataSetChanged();
//                });

        return view;
    }
}
