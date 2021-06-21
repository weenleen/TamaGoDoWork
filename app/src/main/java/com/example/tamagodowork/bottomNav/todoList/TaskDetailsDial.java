package com.example.tamagodowork.bottomNav.todoList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class TaskDetailsDial extends BottomSheetDialogFragment {

    private final String name, deadline, desc, key;
    private Button editBtn, delBtn;
    private ImageButton closeIcon;

    public TaskDetailsDial(String name, String deadline, String desc, String key) {
        this.name = name;
        this.deadline = deadline;
        this.desc = desc;
        this.key = key;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.task_details_dial, null);

        // set the views
        TextView nameView = view.findViewById(R.id.taskName);
        TextView deadlineView = view.findViewById(R.id.taskDeadline);
        TextView descView = view.findViewById(R.id.taskDesc);

        // set the text in the views
        nameView.setText(name);
        deadlineView.setText(deadline);
        descView.setText(desc);

        this.editBtn = view.findViewById(R.id.edit_button);
        this.editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditTaskAct.class);
            intent.putExtra("name", name);
            intent.putExtra("deadline", deadline);
            intent.putExtra("desc", desc);
            intent.putExtra("key", key);
            startActivity(intent);
        });

        // delete button
        this.delBtn = view.findViewById(R.id.delete_button);
        this.delBtn.setOnClickListener(v -> {
            MainActivity.userDoc.collection("Tasks").document(key).delete();
            dismiss();
        });

        // close
        this.closeIcon = view.findViewById(R.id.close_icon);
        closeIcon.setOnClickListener(v -> dismiss());

        builder.setView(view);
        return builder.create();
    }
}