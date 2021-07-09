package com.example.tamagodowork.bottomNav.pet.online;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tamagodowork.R;
import com.example.tamagodowork.authentication.RegisterAct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        // check if logged in
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(AddFriendActivity.this, RegisterAct.class));
            finish(); return;
        }
        String userId = firebaseAuth.getCurrentUser().getUid();
        CollectionReference userData = FirebaseFirestore.getInstance().collection("Users");


        TextView userFriendCode = findViewById(R.id.user_friend_code);
        userFriendCode.setText(userId);

        ImageButton copyButton = findViewById(R.id.friend_code_copy_button);



        EditText searchFriend = findViewById(R.id.friend_code_edit_text);
        ImageButton searchButton = findViewById(R.id.friend_code_search_button);



        RelativeLayout petArea = findViewById(R.id.online_pet_area);



        Button addFriendButton = findViewById(R.id.add_friend_button);


        Button cancelButton = findViewById(R.id.friend_back_button);
        cancelButton.setOnClickListener(v -> startActivity(new Intent(AddFriendActivity.this, OnlineActivity.class)));
    }
}