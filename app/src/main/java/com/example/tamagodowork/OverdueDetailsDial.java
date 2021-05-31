package com.example.tamagodowork;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class OverdueDetailsDial extends BottomSheetDialogFragment {

    private final String name, deadline, desc;
    private ImageButton closeIcon;

    public OverdueDetailsDial(String name, String deadline, String desc) {
        this.name = name;
        this.deadline = deadline;
        this.desc = desc;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.overdue_details_dial, null);

        // set the views
        TextView nameView = view.findViewById(R.id.taskName);
        TextView deadlineView = view.findViewById(R.id.taskDeadline);
        TextView descView = view.findViewById(R.id.taskDesc);

        // set the text in the views
        nameView.setText(name);
        deadlineView.setText(deadline);
        descView.setText(desc);

        // close
        this.closeIcon = view.findViewById(R.id.close_icon);
        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }
}