package com.example.tamagodowork.bottomNav.todoList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
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
    private RelativeLayout layout = null;
    private final MainActivity main;

    public TodoListFrag(@NonNull MainActivity main) {
        this.list = new ArrayList<>();
        this.adapter = new TodoAdapter(main, this.list, TodoAdapter.AdapterType.TASK_LIST);
        this.main = main;

        // Read data from Firestore
        main.userDoc.collection("Todos")
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) return;

                    list.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        list.add(doc.toObject(Todo.class));
                    }

                    Collections.sort(list);
                    adapter.notifyDataSetChanged();

                    if (this.layout != null && list.isEmpty()) {
                        this.layout.setBackground(ResourcesCompat.getDrawable(main.getResources(),
                                R.drawable.wallpaper_nothing_here, null));
                    } else if (this.layout != null) {
                        this.layout.setBackgroundColor(ContextCompat.getColor(main, R.color.off_white));
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.frag_todo_list, container, false);
        this.layout = view.findViewById(R.id.todoList_layout);
        RecyclerView taskListView = view.findViewById(R.id.taskListView);
        taskListView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        taskListView.setAdapter(this.adapter);

        if (list.isEmpty()) {
            this.layout.setBackground(ResourcesCompat.getDrawable(main.getResources(),
                    R.drawable.wallpaper_nothing_here, null));
        } else {
            this.layout.setBackgroundColor(ContextCompat.getColor(main, R.color.off_white));
        }

        return view;
    }
}
