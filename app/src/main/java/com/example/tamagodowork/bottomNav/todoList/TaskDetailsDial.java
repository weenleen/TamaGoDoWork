package com.example.tamagodowork.bottomNav.todoList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class TaskDetailsDial extends BottomSheetDialogFragment {

    private Context context;

    private final String name, deadline, desc, key;
    private final int colourId;

    public TaskDetailsDial(String name, String deadline, String desc, String key, int colourId) {
        this.name = name;
        this.deadline = deadline;
        this.desc = desc;
        this.key = key;
        this.colourId = colourId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.context = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (this.context == null) return builder.create();

        View view = ((FragmentActivity) context).getLayoutInflater().inflate(R.layout.task_details_dial, null);

        // set the views
        TextView nameView = view.findViewById(R.id.taskName);
        TextView deadlineView = view.findViewById(R.id.taskDeadline);
        TextView descView = view.findViewById(R.id.taskDesc);

        // set the text in the views
        nameView.setText(name);
        GradientDrawable indicator = (GradientDrawable) AppCompatResources.getDrawable(context,
                R.drawable.button_color_picker_small);
        if (indicator != null) {
            indicator.setColor(ContextCompat.getColor(context, colourId));
            nameView.setCompoundDrawablesWithIntrinsicBounds(indicator, null, null, null);
        }

        deadlineView.setText(deadline);
        descView.setText(desc);


        // edit button
        Button editBtn = view.findViewById(R.id.edit_button);
        editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditTaskAct.class);
            intent.putExtra("name", name);
            intent.putExtra("deadline", deadline);
            intent.putExtra("desc", desc);
            intent.putExtra("key", key);
            intent.putExtra("colourId", colourId);
            startActivity(intent);
        });



        // delete button
        Button delBtn = view.findViewById(R.id.delete_button);
        delBtn.setOnClickListener(v -> {
            MainActivity.userDoc.collection("Tasks").document(key).delete();
            dismiss();
        });



        // close
        ImageButton closeIcon = view.findViewById(R.id.close_icon);
        closeIcon.setOnClickListener(v -> dismiss());

        builder.setView(view);
        return builder.create();
    }
}