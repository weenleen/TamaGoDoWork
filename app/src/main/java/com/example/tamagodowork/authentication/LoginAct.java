package com.example.tamagodowork.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.google.firebase.auth.FirebaseAuth;


public class LoginAct extends AppCompatActivity {

    EditText editEmail, editPassword;
    Button loginBtn;
    TextView registerLink, resetPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        editEmail = findViewById(R.id.login_email);
        editPassword = findViewById(R.id.login_password);

        loginBtn = findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(v -> {
            String email = editEmail.getText().toString();
            String password = editPassword.getText().toString();

            if (TextUtils.isEmpty(email)) {
                editEmail.setError("Please enter your email");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                editPassword.setError("Please enter your password");
                return;
            }

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        MainActivity.backToMain(getApplicationContext());
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                        Log.e("this", "Exception: "+ Log.getStackTraceString(e));
                    });

        });

        registerLink = findViewById(R.id.register_link);
        registerLink.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RegisterAct.class)));

        resetPwd = findViewById(R.id.reset_link);
        resetPwd.setOnClickListener(v -> {
            final EditText reset = new EditText(v.getContext());
            final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext(), R.style.AlertDialogStyle);
            passwordResetDialog.setTitle("Reset Password");
            passwordResetDialog.setMessage("Enter your email:");
            passwordResetDialog.setView(reset);


            passwordResetDialog.setPositiveButton("Reset", (dialog, which) -> {
                String email = reset.getText().toString();

                firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(unused -> Toast.makeText(LoginAct.this, "Reset Link has been sent to your email", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(LoginAct.this, "Reset attempt unsuccessful.", Toast.LENGTH_SHORT).show());
                dialog.dismiss();
            });

            passwordResetDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            passwordResetDialog.show();
        });

    }
}