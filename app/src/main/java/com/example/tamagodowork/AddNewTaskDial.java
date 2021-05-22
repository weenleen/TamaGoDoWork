package com.example.tamagodowork;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.zip.Inflater;

public class AddNewTaskDial extends BottomSheetDialogFragment {

    EditText editName, editDesc, editDeadline;
    Button saveButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.add_new_task_frag, null);

        this.editName = view.findViewById(R.id.editName);
        this.editDesc = view.findViewById(R.id.editDesc);
        this.editDeadline = view.findViewById(R.id.editDeadline);

        this.saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });

        builder.setView(view);
        return builder.create();
    }
}
