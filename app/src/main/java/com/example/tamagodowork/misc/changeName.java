package com.example.tamagodowork.misc;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.google.firebase.firestore.DocumentReference;

import java.util.Map;


public class changeName extends AppCompatActivity{
    private EditText petName;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        // edit pet name
        EditText editPetName = findViewById(R.id.edit_pet_name);
        Button saveChanges = findViewById(R.id.name_save_button);


        DocumentReference ref = MainActivity.userDoc.collection("Pet").document("Name");
        saveChanges.setOnClickListener(v -> {
                    String name = editPetName.getText().toString();
                    if (!TextUtils.isEmpty(name)) {
                        ref.set(Map.of("name", name));
                        petName.setText(name);
                    }
                }
        );

        MainActivity.userDoc.collection("Pet").document("Name").addSnapshotListener(
                (value, error) -> {
                    if (error != null || value == null) return;

                    String tmp = value.get("name", String.class);
                    if (tmp == null) {
                        petName.setText("");
                        ref.update("name", "");
                        return;
                    }
                    petName.setText(tmp);
                });
    }

}



