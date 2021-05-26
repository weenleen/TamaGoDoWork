package com.example.tamagodowork;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditTaskAct extends AppCompatActivity {

    private EditText editName, editDeadline, editDesc;
    private Button saveBtn, cancelBtn;
    private String key;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        // Set Views
        this.editName = findViewById(R.id.editName);
        this.editDeadline = findViewById(R.id.editDeadline);
        this.editDesc = findViewById(R.id.editDesc);

        // set the text in the views
        this.editName.setText(getIntent().getStringExtra("name"));
        this.editDeadline.setText(getIntent().getStringExtra("deadline"));
        this.editDesc.setText(getIntent().getStringExtra("desc"));

        this.key = getIntent().getStringExtra("key");

        this.saveBtn = findViewById(R.id.save_button);
        this.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Tasks").document(key)
                        .set(new Task(
                                editName.getText().toString(),
                                editDeadline.getText().toString(),
                                editDesc.getText().toString(),
                                key))
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Save Failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                goBack();
            }
        });

        // Cancel Button
        this.cancelBtn = findViewById(R.id.cancel_edit_button);
        this.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    private void goBack() {
        Intent intent = new Intent(EditTaskAct.this, MainActivity.class);
        startActivity(intent);
    }
}