package com.example.tamagodowork.bottomNav.pet.online;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.example.tamagodowork.authentication.RegisterAct;
import com.example.tamagodowork.bottomNav.pet.ProfilePicView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


public class AddFriendActivity extends AppCompatActivity {

    private PetUser friendUser;
    private CurrentUser currentUser;

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

        userData.document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot == null) {
                MainActivity.backToMain(AddFriendActivity.this);
                return;
            }

            this.currentUser = documentSnapshot.toObject(CurrentUser.class);
        });


        // user's code id
        TextView userFriendCode = findViewById(R.id.user_friend_code);
        userFriendCode.setText(userId);

        ImageButton copyButton = findViewById(R.id.friend_code_copy_button);
        copyButton.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("User Friend Code", userId);
            clipboard.setPrimaryClip(clip);
        });




        LinearLayout friendLayout = findViewById(R.id.add_friend_layout);
        friendLayout.setVisibility(View.GONE);

        TextView friendNameText = findViewById(R.id.add_friend_name);
        TextView friendLevelText = findViewById(R.id.add_friend_level);
        RelativeLayout profilePic = findViewById(R.id.add_friend_profile_pic);
        Button addFriendBtn = findViewById(R.id.add_friend_button);


        EditText searchFriendCodeText = findViewById(R.id.friend_code_edit_text);
        ImageButton searchButton = findViewById(R.id.friend_code_search_button);
        searchButton.setOnClickListener(v -> {
            String code = searchFriendCodeText.getText().toString();
            if (code.isEmpty() || code.contentEquals(userId)) return;

            userData.document(code).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot == null || !documentSnapshot.exists()) {
                    friendLayout.setVisibility(View.GONE);
                    friendUser = null;
                    Toast.makeText(AddFriendActivity.this, "User not found :(", Toast.LENGTH_SHORT).show();
                    return;
                }

                friendUser = new PetUser(
                        code,
                        documentSnapshot.get("Name", String.class),
                        documentSnapshot.get("XP", Integer.class),
                        userData.document(code));

                friendLayout.setVisibility(View.VISIBLE);

                if (currentUser.getFriendsList().contains(code)) {
                    addFriendBtn.setText(getString(R.string.already_friend, friendUser.getName()));
                    addFriendBtn.setClickable(false);
                } else if (currentUser.getSentRequests().contains(code)) {
                    addFriendBtn.setText(getString(R.string.friend_request_sent));
                    addFriendBtn.setClickable(false);
                } else {
                    addFriendBtn.setText(getString(R.string.friend_request_button));
                    addFriendBtn.setClickable(true);
                }

                friendNameText.setText(friendUser.getName());
                friendLevelText.setText(getString(R.string.level, friendUser.getLevel()));

                friendUser.getTask().addOnCompleteListener(task -> {
                    ProfilePicView pfp = ProfilePicView.largeInstance(AddFriendActivity.this, friendUser.getPet());
                    profilePic.addView(pfp);
                });
            });

        });


        addFriendBtn.setOnClickListener(v -> {
            userData.document(userId).update("sentRequests",
                    FieldValue.arrayUnion(friendUser.getId()));
            userData.document(friendUser.getId()).update("receivedRequests",
                    FieldValue.arrayUnion(userId));
            currentUser.getSentRequests().add(friendUser.getId());
            addFriendBtn.setText(getString(R.string.friend_request_sent));
            addFriendBtn.setClickable(false);
            Toast.makeText(getApplicationContext(), "Friend Request Sent!", Toast.LENGTH_SHORT).show();
        });


        Button cancelButton = findViewById(R.id.friend_back_button);
        cancelButton.setOnClickListener(v -> startActivity(new Intent(AddFriendActivity.this, OnlineActivity.class)));
    }
}