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
import com.example.tamagodowork.SettingsAct;
import com.google.firebase.firestore.DocumentReference;

import java.util.Map;


public class ChangeName extends AppCompatActivity{

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        // edit pet name
        EditText editPetName = findViewById(R.id.edit_pet_name);
        Button saveChanges = findViewById(R.id.name_save_button);
        Button cancelChanges = findViewById(R.id.cancel_name_button);
        saveChanges.setOnClickListener(v -> saveName(editPetName));
        cancelChanges.setOnClickListener(v -> goBack());
    }

    public void saveName(EditText editPetName) {
        DocumentReference ref = MainActivity.userDoc.collection("Pet").document("Name");
        String name = editPetName.getText().toString();
        if (!TextUtils.isEmpty(name)) {
            ref.set(Map.of("name", name));
        }
        goBack();
    }

    public void goBack() {
        MainActivity.backToMain(ChangeName.this);
        finish();
    }

}



