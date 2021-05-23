package com.example.tamagodowork;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.zip.Inflater;

public class TaskDetailsDial extends BottomSheetDialogFragment {

    private TextView nameView, deadlineView, descView;
    private Button editBtn, delBtn;
    private DatabaseReference reference;
    private String name, deadline, desc, key;

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
        this.nameView = view.findViewById(R.id.taskName);
        this.deadlineView = view.findViewById(R.id.taskDeadline);
        this.descView = view.findViewById(R.id.taskDesc);

        // set the text in the views
        this.nameView.setText(name);
        this.deadlineView.setText(deadline);
        this.descView.setText(desc);

        // Get data from firebase
        this.reference = FirebaseDatabase.getInstance().getReference()
                .child("TamaGoDoWork").child(this.key);

        // edit button
        this.editBtn = view.findViewById(R.id.edit_button);
        this.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditTaskAct.class);
                intent.putExtra("key", key);
                intent.putExtra("name", name);
                intent.putExtra("deadline", deadline);
                intent.putExtra("desc", desc);
                startActivity(intent);
            }
        });

        // TODO
        // delete button
        this.delBtn = view.findViewById(R.id.delete_button);
        this.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                        if (task.isSuccessful()) {
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), "Delete Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        builder.setView(view);
        return builder.create();
    }
}