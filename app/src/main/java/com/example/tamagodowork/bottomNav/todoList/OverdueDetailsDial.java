package com.example.tamagodowork.bottomNav.todoList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.tamagodowork.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class OverdueDetailsDial extends BottomSheetDialogFragment {

    private final String name, deadline, desc;
    private final int colourId;

    public OverdueDetailsDial(String name, String deadline, String desc, int colourId) {
        this.name = name;
        this.deadline = deadline;
        this.desc = desc;
        this.colourId = colourId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        FragmentActivity context = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (context == null) return builder.create();
        View view = context.getLayoutInflater().inflate(R.layout.overdue_details_dial, null);

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

        // close
        ImageButton closeIcon = view.findViewById(R.id.close_icon);
        closeIcon.setOnClickListener(v -> dismiss());

        builder.setView(view);
        return builder.create();
    }
}