package com.example.tamagodowork;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditTaskAct extends AppCompatActivity {

    private EditText editName, editDeadline, editDesc;
    private Button saveBtn, cancelBtn;
    // private DatabaseReference reference;
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

//        // retrieve data
//        reference = FirebaseDatabase.getInstance().getReference()
//                .child("TamaGoDoWork").child(this.key);
//
//        // Save Button
//        this.saveBtn = findViewById(R.id.save_button);
//        this.saveBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                reference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        snapshot.getRef().child("taskName").setValue(editName.getText().toString());
//                        snapshot.getRef().child("taskDeadline").setValue(editDeadline.getText().toString());
//                        snapshot.getRef().child("taskDesc").setValue(editDesc.getText().toString());
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(getApplicationContext(), "Save Failed", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                goBack();
//            }
//        });

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