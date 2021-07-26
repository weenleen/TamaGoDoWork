package com.example.tamagodowork.bottomNav.pet.online;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tamagodowork.R;
import com.example.tamagodowork.bottomNav.pet.Pet;
import com.example.tamagodowork.bottomNav.pet.PetCanvas;
import com.example.tamagodowork.bottomNav.pet.RoomActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class VisitFriend extends AppCompatActivity {

    private String userId;
    private PetUser user;
    private Pet pet;
    private Integer wallpaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_friend);

        userId = getIntent().getStringExtra("userId");
        if (userId == null) {
            startActivity(new Intent(VisitFriend.this, OnlineActivity.class));
            this.finish();
            Log.e("visit Friend", "ID not found");
        }


        RelativeLayout petArea = findViewById(R.id.visit_pet_area);
        ImageView wallpaperImage = findViewById(R.id.visit_wallpaper_image);
        TextView userNameTextView = findViewById(R.id.visit_name_textView);
        TextView levelTextView = findViewById(R.id.visit_level_textView);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Users").document(userId);


        userRef.get().addOnSuccessListener(documentSnapshot -> {
            user = new PetUser(userId,
                    documentSnapshot.get("Name", String.class),
                    documentSnapshot.get("XP", Integer.class),
                    userRef);

            userNameTextView.setText(user.getName());
            levelTextView.setText(getString(R.string.level, user.getLevel()));
        });



        userRef.collection("Pet").get().addOnCompleteListener(t -> {
            if (!t.isSuccessful()) return;
            else if (t.getResult() == null) return;

            List<DocumentSnapshot> documents = t.getResult().getDocuments();

            for (DocumentSnapshot snapshot: documents) {
                String id = snapshot.getId();
                switch(id) {
                    case "Room": {
                        this.wallpaper = snapshot.get("wallpaper", Integer.class);
                        Drawable drawable;
                        if (wallpaper != null && wallpaper >= 0 && wallpaper < RoomActivity.wallpapers.length) {
                            try {
                                drawable = AppCompatResources.getDrawable(VisitFriend.this, RoomActivity.wallpapers[wallpaper]);
                            } catch (Exception e) { return; }
                        } else {
                            drawable = AppCompatResources.getDrawable(VisitFriend.this, RoomActivity.wallpapers[0]);
                            userRef.collection("Pet").document("Room").update("wallpaper", 0);
                        }
                        wallpaperImage.setImageDrawable(drawable);
                        break;
                    }
                    case "Customisation": {
                        this.pet = snapshot.toObject(Pet.class);
                        if (this.pet == null) {
                            this.pet = Pet.defaultPet();
                            userRef.collection("Pet").document("Customisation")
                                    .set(pet);
                        }
                        PetCanvas petCanvas = new PetCanvas(VisitFriend.this, pet);
                        if (petCanvas.getParent() != null) {
                            ((ViewGroup) petCanvas.getParent()).removeView(petCanvas);
                        }
                        petArea.addView(petCanvas);
                        break;
                    }
                }
            }
        });


        Button homeButton = findViewById(R.id.visit_home_button);
        homeButton.setOnClickListener(v -> {
            startActivity(new Intent(VisitFriend.this, OnlineActivity.class));
            finish();
        });
    }
}