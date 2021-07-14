package com.example.tamagodowork.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
//import android.widget.Toast;


//import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.example.tamagodowork.SettingsAct;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentReference;

import org.jetbrains.annotations.NotNull;



public class DeleteAccountAct extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_confirmation);

        Button backButton = findViewById(R.id.cancel_delete_button);
        //Button confirmButton = findViewById(R.id.confirm_delete_button);


        // back button
        backButton.setOnClickListener(v -> startActivity(new Intent(DeleteAccountAct.this, SettingsAct.class)));

        // confirm button
        /*
        confirmButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        String USER_ID = FirebaseAuth.getInstance().getUid();
                        CollectionReference toDoes = MainActivity.userDoc.collection("Todos");
                        CollectionReference pet = MainActivity.userDoc.collection("Pet");

                        toDoes.get().then(querySnapshot => {
                            querySnapshot.docs.forEach(snapshot => {
                                snapshot.ref.delete();
                            })
                         })

                         pet.get().then(querySnapshot => {
                            querySnapshot.docs.forEach(snapshot => {
                                snapshot.ref.delete();
                            })
                         })




                        Toast.makeText(DeleteAccountAct.this,"Account Deleted Successfully",Toast.LENGTH_SHORT).show();
                        // Upon Successful Deletion, redirect the user to the register page
                        startActivity(new Intent(DeleteAccountAct.this, RegisterAct.class));
                    } else {
                        Toast.makeText(DeleteAccountAct.this, task.getException().getMessage(), Toast.LENGTH_SHORT);
                    }
                }
            });
        });

        */

    }
}
