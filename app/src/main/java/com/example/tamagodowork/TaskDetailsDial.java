package com.example.tamagodowork;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class TaskDetailsDial extends BottomSheetDialogFragment {

    private final String name, deadline, desc, key;

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

        Button editBtn = view.findViewById(R.id.edit_button);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditTaskAct.class);
                intent.putExtra("name", name);
                intent.putExtra("deadline", deadline);
                intent.putExtra("desc", desc);
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });

        // delete button
        Button delBtn = view.findViewById(R.id.delete_button);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.userDoc.collection("Tasks").document(key).delete();
                dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }
}