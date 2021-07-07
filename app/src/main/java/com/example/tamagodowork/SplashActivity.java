package com.example.tamagodowork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.tamagodowork.authentication.RegisterAct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/** Create splash screen before the main activity **/
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getUid() != null) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Users").document(firebaseAuth.getUid()).get().addOnSuccessListener(documentSnapshot -> {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("selectedFrag", documentSnapshot.get("selectedFrag", Integer.class));
                    intent.putExtra("XP", documentSnapshot.get("XP", Integer.class));
                    startActivity(intent);
                    finish();
                });
            } else {
                startActivity(new Intent(SplashActivity.this, RegisterAct.class));
                finish();
            }
        }, 500);
    }
}