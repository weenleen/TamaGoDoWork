package com.example.tamagodowork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.tamagodowork.authentication.*;
import com.example.tamagodowork.misc.ChangeName;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button resetBtn = findViewById(R.id.btn_reset);
        resetBtn.setOnClickListener(v -> {
            MainActivity.setXP(0);
            MainActivity.backToMain(SettingsAct.this);
        });

//        this.themesBtn = findViewById(R.id.btn_themes);

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
        Button changeNameBtn = findViewById(R.id.change_pet_name);
        changeNameBtn.setOnClickListener(v -> {
            startActivity(new Intent(SettingsAct.this, ChangeName.class));
            finish();
        });


        Button backBtn = findViewById(R.id.btn_back);
        backBtn.setOnClickListener(v -> MainActivity.backToMain(SettingsAct.this));
    }
}