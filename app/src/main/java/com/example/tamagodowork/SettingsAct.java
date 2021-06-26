package com.example.tamagodowork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.tamagodowork.authentication.*;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsAct extends AppCompatActivity {

    Button resetBtn, themesBtn, logoutBtn, backBtn, chgPwdBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.resetBtn = findViewById(R.id.btn_reset);
        this.resetBtn.setOnClickListener(v -> {
            MainActivity.setXP(0);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });

//        this.themesBtn = findViewById(R.id.btn_themes);

        this.logoutBtn = findViewById(R.id.btn_logout);
        this.logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), LoginAct.class));
            finish();
        });

        // change pwd button
        this.chgPwdBtn = findViewById(R.id.change_pwd);
        this.chgPwdBtn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ChangePasswordAct.class));
            finish();
        });

        this.backBtn = findViewById(R.id.btn_back);
        this.backBtn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });
    }
}