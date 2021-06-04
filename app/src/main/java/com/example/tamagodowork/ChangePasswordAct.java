package com.example.tamagodowork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class ChangePasswordAct extends AppCompatActivity {
    private EditText newPwd, oldPwd;
    private Button saveBtn, cancelBtn;
    FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        // retrieve current user
        this.oldPwd = findViewById(R.id.oldPwd);
        this.newPwd = findViewById(R.id.newPwd);
        this.saveBtn = findViewById(R.id.pwd_save_button);
        this.cancelBtn = findViewById(R.id.cancel_button);



        this.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });
 

        this.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.backToMain(ChangePasswordAct.this);
            }
        });

    }

    public void updatePassword() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();
        String newPassword = newPwd.getText().toString();
        AuthCredential credential = EmailAuthProvider.getCredential(email, oldPwd.getText().toString());


        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ChangePasswordAct.this, "Password is updated. Login with new password", Toast.LENGTH_LONG).show();
                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(new Intent(getApplicationContext(), LoginAct.class));
                                        finish();
                                    } else {
                                        Toast.makeText(ChangePasswordAct.this, "Failed to update password!", Toast.LENGTH_LONG).show();
                                        MainActivity.backToMain(ChangePasswordAct.this);
                                    }
                                }});

                } else {
                    Toast.makeText(ChangePasswordAct.this, "Authentication Failed!", Toast.LENGTH_LONG).show();
                    MainActivity.backToMain(ChangePasswordAct.this);
                }
            }
        });
    }
}

