package com.example.tamagodowork.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tamagodowork.SettingsAct;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordAct extends AppCompatActivity {
    private EditText newPwd, oldPwd;
    FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        // retrieve current user
        this.oldPwd = findViewById(R.id.oldPwd);
        this.newPwd = findViewById(R.id.newPwd);
        Button saveBtn = findViewById(R.id.pwd_save_button);
        Button cancelBtn = findViewById(R.id.cancel_button);

        saveBtn.setOnClickListener(v -> updatePassword());

        cancelBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SettingsAct.class)));

    }

    public void updatePassword() {
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) return;

        final String email = user.getEmail();
        if (email == null) return;

        String newPassword = newPwd.getText().toString();
        AuthCredential credential = EmailAuthProvider.getCredential(email, oldPwd.getText().toString());


        user.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user.updatePassword(newPassword)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(ChangePasswordAct.this, "Password is updated. Login with new password", Toast.LENGTH_LONG).show();
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getApplicationContext(), LoginAct.class));
                                finish();
                            } else {
                                Toast.makeText(ChangePasswordAct.this, "Failed to update password!", Toast.LENGTH_LONG).show();
                                MainActivity.backToMain(ChangePasswordAct.this);
                            }
                        });

            } else {
                Toast.makeText(ChangePasswordAct.this, "Authentication Failed!", Toast.LENGTH_LONG).show();
                MainActivity.backToMain(ChangePasswordAct.this);
            }
        });
    }
}

