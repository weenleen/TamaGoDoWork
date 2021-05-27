package com.example.tamagodowork;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class AddTaskAct extends AppCompatActivity {

    private EditText addName, addDeadline, addDesc;
    private Button createBtn, cancelBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        this.addName = findViewById(R.id.addName);
        this.addDeadline = findViewById(R.id.addDeadline);
        this.addDesc = findViewById(R.id.addDesc);

        this.createBtn = findViewById(R.id.create_button);
        this.createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference ref = MainActivity.userDoc.collection("Tasks").document();

                String name = addName.getText().toString();
                String deadline = addDeadline.getText().toString();
                String desc = addDesc.getText().toString();
                String key = ref.getId();

                if (TextUtils.isEmpty(name)) {
                    addName.setError("Please enter a name");
                    return;
                } else if (TextUtils.isEmpty(deadline)) {
                    addDeadline.setError("Please enter a deadline");
                    return;
                }

                ref.set(new Task(name, deadline, desc, key));

                goBack();
            }
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