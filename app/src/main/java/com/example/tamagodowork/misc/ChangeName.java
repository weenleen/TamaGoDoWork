package com.example.tamagodowork.misc;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.example.tamagodowork.authentication.RegisterAct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class ChangeName extends AppCompatActivity{

    private DocumentReference userData;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        // check if logged in
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(ChangeName.this, RegisterAct.class));
            finish(); return;
        }
        String userId = firebaseAuth.getCurrentUser().getUid();
        userData = FirebaseFirestore.getInstance().collection("Users").document(userId);


        EditText editUserName = findViewById(R.id.edit_user_name);
        Button saveChanges = findViewById(R.id.name_save_button);
        Button cancelChanges = findViewById(R.id.cancel_name_button);

        userData.get().addOnSuccessListener(documentSnapshot -> {
            String oldName = documentSnapshot.get("Name", String.class);
            editUserName.setText(oldName);
        });

        // edit pet name
        saveChanges.setOnClickListener(v -> saveName(editUserName));
        cancelChanges.setOnClickListener(v -> goBack());
    }

    public void saveName(EditText editUserName) {
        String name = editUserName.getText().toString();
        if (!TextUtils.isEmpty(name)) {
            userData.update("Name", name);
        }
        goBack();
    }

    public void goBack() {
        MainActivity.backToMain(ChangeName.this);
    }
}



