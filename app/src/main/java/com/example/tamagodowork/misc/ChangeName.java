package com.example.tamagodowork.misc;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;


public class ChangeName extends AppCompatActivity{

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        // edit pet name
        EditText editUserName = findViewById(R.id.edit_user_name);
        Button saveChanges = findViewById(R.id.name_save_button);
        Button cancelChanges = findViewById(R.id.cancel_name_button);
        saveChanges.setOnClickListener(v -> saveName(editUserName));
        cancelChanges.setOnClickListener(v -> goBack());
    }

    public void saveName(EditText editUserName) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userData = FirebaseFirestore.getInstance().collection("Users").document(userId);
        String name = editUserName.getText().toString();
        if (!TextUtils.isEmpty(name)) {
            userData.set(Map.of("Name", name));
        }
        goBack();
    }

    public void goBack() {
        MainActivity.backToMain(ChangeName.this);
        finish();
    }

}



