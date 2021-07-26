package com.example.tamagodowork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;


/** Create splash screen before the main activity **/
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> MainActivity.backToMain(SplashActivity.this), 200);
    }
}