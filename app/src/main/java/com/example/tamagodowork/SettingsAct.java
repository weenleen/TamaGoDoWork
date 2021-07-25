package com.example.tamagodowork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tamagodowork.authentication.*;
import com.example.tamagodowork.bottomNav.pet.Pet;
import com.example.tamagodowork.bottomNav.pet.ProfilePicView;
import com.example.tamagodowork.misc.ChangeName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        // check if logged in
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(SettingsAct.this, RegisterAct.class));
            finish(); return;
        }
        String userId = firebaseAuth.getCurrentUser().getUid();



        TextView nameTextView = findViewById(R.id.settings_username);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Users").document(userId);
        ref.get().addOnSuccessListener(documentSnapshot ->
                nameTextView.setText(documentSnapshot.get("Name", String.class)));



        RelativeLayout profilePicLayout = findViewById(R.id.settings_profile_pic);

        ref.collection("Pet").document("Customisation").get()
                .addOnSuccessListener(documentSnapshot -> {
                    Pet pet = documentSnapshot.toObject(Pet.class);
                    if (pet == null) {
                        pet = Pet.defaultPet();
                        ref.collection("Pet").document("Customisation")
                                .set(pet);
                    }
                    profilePicLayout.addView(ProfilePicView.largeInstance(SettingsAct.this, pet));
                });



        Button deleteAccount = findViewById(R.id.btn_delete);
        deleteAccount.setOnClickListener( v -> startActivity(new Intent(SettingsAct.this, DeleteAccountAct.class)) );

        Button resetBtn = findViewById(R.id.btn_reset);
        resetBtn.setOnClickListener(v -> {
            MainActivity.setXP(0);
            MainActivity.backToMain(SettingsAct.this);
        });

        Button logoutBtn = findViewById(R.id.btn_logout);
        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(SettingsAct.this, LoginAct.class));
            finish();
        });

        // change pwd button
        Button chgPwdBtn = findViewById(R.id.change_pwd);
        chgPwdBtn.setOnClickListener(v -> {
            startActivity(new Intent(SettingsAct.this, ChangePasswordAct.class));
            finish();
        });


        //change pet name button
        Button changeNameBtn = findViewById(R.id.change_user_name);
        changeNameBtn.setOnClickListener(v -> {
            startActivity(new Intent(SettingsAct.this, ChangeName.class));
            finish();
        });


        Button backBtn = findViewById(R.id.btn_back);
        backBtn.setOnClickListener(v -> MainActivity.backToMain(SettingsAct.this));
    }
}