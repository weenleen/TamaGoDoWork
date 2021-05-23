package com.example.tamagodowork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTaskAct extends AppCompatActivity {

    private EditText addName, addDeadline, addDesc;
    private Button createBtn, cancelBtn;
    private DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        reference = FirebaseDatabase.getInstance().getReference().child("TamaGoDoWork");

        this.addName = findViewById(R.id.addName);
        this.addDeadline = findViewById(R.id.addDeadline);
        this.addDesc = findViewById(R.id.addDesc);

        this.createBtn = findViewById(R.id.create_button);
        this.createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                insertData();
            }

            public void insertData() {
                String name = addName.getText().toString();
                String desc = addDesc.getText().toString();
                String deadline = addDeadline.getText().toString();
                Task newTask = new Task(name, desc, deadline);
                // use push method to generate new unique id so the data won't be overrided
                reference.push().setValue(newTask);
            }
        });





        this.cancelBtn = findViewById(R.id.cancel_button);
        this.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTaskAct.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}