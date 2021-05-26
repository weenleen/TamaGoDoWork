package com.example.tamagodowork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class RegisterAct extends AppCompatActivity {

    EditText editName, editEmail, editPassword1, editPassword2;
    Button loginGoToBtn, registerBtn;

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
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                String email = editEmail.getText().toString();
                String password1 = editPassword1.getText().toString();
                String password2 = editPassword2.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    editName.setError("Please enter your name");
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

                firebaseAuth.createUserWithEmailAndPassword(email, password2)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                HashMap<String, Object> userData = new HashMap<>();
                                userData.put("Name", name);
                                userData.put("XP", 0);

                                db.collection("Users").document(authResult.getUser().getUid())
                                        .set(userData);

                                Toast.makeText(getApplicationContext(), "User Registered", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), LoginAct.class));
                            }})
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        loginGoToBtn = findViewById(R.id.btn_go_to_login);
        loginGoToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginAct.class));
            }
        });
    }
}