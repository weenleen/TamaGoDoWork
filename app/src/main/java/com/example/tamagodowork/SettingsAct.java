package com.example.tamagodowork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsAct extends AppCompatActivity {

    Button resetBtn, themesBtn, logoutBtn, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.resetBtn = findViewById(R.id.btn_reset);
        this.resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.xp = 0;
                MainActivity.userDoc.update("XP", 0);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        this.themesBtn = findViewById(R.id.btn_themes);

        this.logoutBtn = findViewById(R.id.btn_logout);
        this.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginAct.class));
                finish();
            }
        });

        this.backBtn = findViewById(R.id.btn_back);
        this.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }
}