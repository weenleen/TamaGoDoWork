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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamagodowork.R;
import com.example.tamagodowork.authentication.RegisterAct;
import com.example.tamagodowork.bottomNav.pet.Pet;
import com.example.tamagodowork.bottomNav.pet.PetCanvas;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddFriendActivity extends AppCompatActivity {

    PetUser friendUser;
    Pet friendPet;
    PetCanvas petCanvas;

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
        copyButton.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("User Friend Code", userId);
            clipboard.setPrimaryClip(clip);
        });


        RelativeLayout petArea = findViewById(R.id.friend_pet_area);


        Button addFriendButton = findViewById(R.id.add_friend_button);
        addFriendButton.setOnClickListener(v -> {
            if (friendUser == null) {
                Toast.makeText(getApplicationContext(), "Failed to send Friend Request", Toast.LENGTH_SHORT).show();
                return;
            } else if (friendUser.getId() == null) {
                Toast.makeText(getApplicationContext(), "That's your own ID!", Toast.LENGTH_SHORT).show();
                return;
            }
            userData.document(userId).update("sentRequests",
                    FieldValue.arrayUnion(friendUser.getId()));
            userData.document(friendUser.getId()).update("receivedRequests",
                    FieldValue.arrayUnion(userId));
            Toast.makeText(getApplicationContext(), "Friend Request Sent!", Toast.LENGTH_SHORT).show();
        });
        addFriendButton.setVisibility(View.GONE);


        EditText searchFriendCodeText = findViewById(R.id.friend_code_edit_text);
        ImageButton searchButton = findViewById(R.id.friend_code_search_button);
        searchButton.setOnClickListener(v -> {
            String code = searchFriendCodeText.getText().toString();
            if (code.isEmpty() || code.contentEquals(userId)) return;

            userData.document(code).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot == null || !documentSnapshot.exists()) {
                    addFriendButton.setVisibility(View.GONE);
                    friendPet = null;
                    friendUser = null;
                    petArea.removeAllViews();
                    return;
                }

                friendUser = new PetUser(
                        code,
                        documentSnapshot.get("Name", String.class),
                        documentSnapshot.get("XP", Integer.class));

                addFriendButton.setText(getString(R.string.friend_request_button, friendUser.getName()));
                addFriendButton.setVisibility(View.VISIBLE);
            });


            DocumentReference ref = userData.document(code).collection("Pet").document("Customisation");
            ref.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot == null || !documentSnapshot.exists()) return;
                friendPet = documentSnapshot.toObject(Pet.class);

                if (friendPet == null) {
                    friendPet = Pet.defaultPet();
                    ref.set(friendPet);
                }
                petCanvas = new PetCanvas(AddFriendActivity.this, friendPet);
                petArea.addView(petCanvas);
            });
        });



        Button cancelButton = findViewById(R.id.friend_back_button);
        cancelButton.setOnClickListener(v -> startActivity(new Intent(AddFriendActivity.this, OnlineActivity.class)));
    }
}