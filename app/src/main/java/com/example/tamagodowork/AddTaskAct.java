package com.example.tamagodowork;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;


public class AddTaskAct extends AppCompatActivity {

    private EditText addName, addDeadline, addDesc;
    private Button createBtn, cancelBtn;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        db = FirebaseFirestore.getInstance();

        this.addName = findViewById(R.id.addName);
        this.addDeadline = findViewById(R.id.addDeadline);
        this.addDesc = findViewById(R.id.addDesc);

        this.createBtn = findViewById(R.id.create_button);
        this.createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = addName.getText().toString();
                String deadline = addDeadline.getText().toString();
                String desc = addDesc.getText().toString();

                //insertData();
                goBack();
            }

//            public void insertData() {
//                String name = addName.getText().toString();
//                String deadline = addDeadline.getText().toString();
//                String desc = addDesc.getText().toString();
//                DatabaseReference refKey = reference.push();
//                Task newTask = new Task(name, deadline, desc, refKey.getKey());
//                // use push method to generate new unique id so the data won't be overridden
//                refKey.setValue(newTask);
//            }
        });


        this.cancelBtn = findViewById(R.id.cancel_add_button);
        this.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    private void goBack() {
        Intent intent = new Intent(AddTaskAct.this, MainActivity.class);
        startActivity(intent);
    }
}