package com.example.tamagodowork;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.zip.Inflater;

public class TaskDetailsDial extends BottomSheetDialogFragment {

    private TextView taskName, taskDesc, taskDeadline;
    private Button editBtn, delBtn;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.task_details_dial, null);

        this.taskName = view.findViewById(R.id.taskName);
        this.taskDesc = view.findViewById(R.id.taskDesc);
        this.taskDeadline = view.findViewById(R.id.taskDeadline);

        this.editBtn = view.findViewById(R.id.edit_button);
        this.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });

        this.delBtn = view.findViewById(R.id.delete_button);
        this.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });

        builder.setView(view);
        return builder.create();
    }
}
