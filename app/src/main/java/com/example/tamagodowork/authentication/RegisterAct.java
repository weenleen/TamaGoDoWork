package com.example.tamagodowork.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamagodowork.R;
import com.example.tamagodowork.bottomNav.pet.Pet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterAct extends AppCompatActivity {

    EditText editName, editEmail, editPassword1, editPassword2;
    Button registerBtn;
    TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        editName = findViewById(R.id.register_name);
        editEmail = findViewById(R.id.register_email);
        editPassword1 = findViewById(R.id.register_password1);
        editPassword2 = findViewById(R.id.register_password2);

        registerBtn = findViewById(R.id.btn_register);
        registerBtn.setOnClickListener(v -> {
            String name = editName.getText().toString();
            String email = editEmail.getText().toString();
            String password1 = editPassword1.getText().toString();
            String password2 = editPassword2.getText().toString();

            if (TextUtils.isEmpty(name)) {
                editName.setError("Please enter your name");
                return;
            } else if (name.length() < 6) {
                editName.setError("Name must be more than 6 characters");
                return;
            }

            if (TextUtils.isEmpty(email)) {
                editEmail.setError("Please enter your email");
                return;
            }

            if (TextUtils.isEmpty(password1)) {
                editPassword1.setError("Please enter your password");
                return;
            }

            if (TextUtils.isEmpty(password2)) {
                editPassword2.setError("Please enter your password");
                return;
            }

            if (!password1.equals(password2)) {
                editPassword1.setError("Passwords do not match");
                editPassword2.setError("Passwords do not match");
                return;
            }

            if (password1.length() < 8) {
                editPassword1.setError("Password must be longer than 8 characters");
                editPassword2.setError("Password must be longer than 8 characters");
                return;
            }

            firebaseAuth.createUserWithEmailAndPassword(email, password2)
                    .addOnSuccessListener(authResult -> {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        FirebaseUser user = authResult.getUser();

                        if (user == null) return;

                        HashMap<String, Object> userData = new HashMap<>();
                        userData.put("Name", name);
                        userData.put("XP", 0);
                        userData.put("friendsList", List.of("xKFyuccBjtQdemv9pNm94mMpFHE3"));
                        userData.put("receivedRequests", List.of("xW2sWcoFThfTCHlnxEqMfTo9OQu2"));
                        userData.put("sentRequests", new ArrayList<String>());

                        db.collection("Users").document(user.getUid())
                                .set(userData);

                        db.collection("Users").document(user.getUid()).collection("Pet").document("Customisation")
                                .set(Pet.defaultPet());

                        db.collection("Users").document(user.getUid()).collection("Pet").document("Room")
                                .set(Map.of("wallpaper", 0));

                        Toast.makeText(getApplicationContext(), "User Registered", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterAct.this, LoginAct.class));
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(RegisterAct.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        loginLink = findViewById(R.id.login_link);
        loginLink.setOnClickListener(v -> {
            startActivity(new Intent(RegisterAct.this, LoginAct.class));
            finish();
        });
    }
}