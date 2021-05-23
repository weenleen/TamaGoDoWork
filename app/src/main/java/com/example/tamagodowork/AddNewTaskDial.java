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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.zip.Inflater;

public class AddNewTaskDial extends BottomSheetDialogFragment {

    DatabaseReference taskDb;
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
        taskDb = FirebaseDatabase.getInstance().getReference().child("TamaGoDoWork");
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                insertData();
            }
        });

        builder.setView(view);
        return builder.create();
    }

    // method that is used to insert data from the dial to the firebase database
    public void insertData() {
        String name = this.editName.getText().toString();
        String desc = this.editDesc.getText().toString();
        String deadline = this.editDeadline.getText().toString();
        Task newTask = new Task(name, desc, deadline);
        // use push method to generate new unique id so the data won't be overrided
        taskDb.push().setValue(newTask);
    }
}
