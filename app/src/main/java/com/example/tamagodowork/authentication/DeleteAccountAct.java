package com.example.tamagodowork.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.example.tamagodowork.SettingsAct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;





public class DeleteAccountAct extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_confirmation);

        Button backButton = findViewById(R.id.cancel_delete_button);
        Button confirmButton = findViewById(R.id.confirm_delete_button);



        // back button
        backButton.setOnClickListener(v -> startActivity(new Intent(DeleteAccountAct.this, SettingsAct.class)));

        // confirm button
        confirmButton.setOnClickListener(v -> {
            // get current users id
            try {

                String USER_ID = FirebaseAuth.getInstance().getUid();
                // get collection reference to the current user


                DocumentReference user = FirebaseFirestore.getInstance().collection("Users").document(USER_ID);

                DocumentReference customisation = MainActivity.userDoc.collection("Pet").document("Customisation");
                DocumentReference room = MainActivity.userDoc.collection("Pet").document("Room");
                CollectionReference toDoes = MainActivity.userDoc.collection("Todos");


                // delete things for the pet fragment
                room.delete()
                        .addOnSuccessListener(success -> Toast.makeText(DeleteAccountAct.this, "Document successfully deleted", Toast.LENGTH_SHORT))
                        .addOnFailureListener(failure -> Toast.makeText(DeleteAccountAct.this, failure.getMessage(), Toast.LENGTH_SHORT));


                customisation.delete()
                        .addOnSuccessListener(success -> Toast.makeText(DeleteAccountAct.this, "Document successfully deleted", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(failure -> Toast.makeText(DeleteAccountAct.this, failure.getMessage(), Toast.LENGTH_SHORT).show());

                // recursively delete things for the task fragment
                toDoes.get()
                        .addOnSuccessListener(queryDocumentSnapshots -> queryDocumentSnapshots.getDocuments().forEach(documentSnapshot -> documentSnapshot.getReference().delete()))
                        .addOnFailureListener(failure -> Toast.makeText(DeleteAccountAct.this, failure.getMessage(), Toast.LENGTH_SHORT).show());

                // finally delete the user from firestore
                user.delete()
                        .addOnSuccessListener(success -> Toast.makeText(DeleteAccountAct.this, "User successfully deleted", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(failure ->  Toast.makeText(DeleteAccountAct.this, failure.getMessage(), Toast.LENGTH_SHORT).show());

            } catch (Exception e) {
                Toast.makeText(DeleteAccountAct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(DeleteAccountAct.this, "Account Deleted Successfully", Toast.LENGTH_SHORT).show();
                            // Upon Successful Deletion, redirect the user to the register page
                            startActivity(new Intent(DeleteAccountAct.this, RegisterAct.class));
                        } else {
                            Toast.makeText(DeleteAccountAct.this, "Account Deletion Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    });

            }
        });





    }
}
