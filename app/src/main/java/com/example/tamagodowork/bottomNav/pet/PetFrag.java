package com.example.tamagodowork.bottomNav.pet;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.example.tamagodowork.bottomNav.pet.online.OnlineActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.List;


public class PetFrag extends Fragment {

    private Integer wallpaper;
    private final MainActivity main;
    private final CollectionReference ref;

    private Pet pet;
    private PetCanvas petCanvas;
    private final Task<QuerySnapshot> task;

    public PetFrag(@NonNull MainActivity main) {
        this.main = main;

        this.ref = MainActivity.userDoc.collection("Pet");
        this.task = ref.get().addOnCompleteListener(t -> {
            if (!t.isSuccessful()) return;
            else if (t.getResult() == null) return;

            List<DocumentSnapshot> documents = t.getResult().getDocuments();

            for (DocumentSnapshot snapshot: documents) {
                String id = snapshot.getId();
                switch(id) {
                    case "Room": {
                        this.wallpaper = snapshot.get("wallpaper", Integer.class);
                        break;
                    }
                    case "Customisation": {
                        this.pet = snapshot.toObject(Pet.class);
                        break;
                    }
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_pet, container, false);

        //  getting data
        ImageView wallpaperBG = view.findViewById(R.id.pet_frag_wallpaper);
        RelativeLayout relativeLayout = view.findViewById(R.id.pet_area);

        this.task.addOnCompleteListener(task -> {

            if (this.pet == null) {
                this.pet = Pet.defaultPet();
                ref.document("Customisation").set(pet);
            }
            this.petCanvas = new PetCanvas(main, pet);

            if (petCanvas.getParent() != null) {
                ((ViewGroup) petCanvas.getParent()).removeView(petCanvas);
            }
            relativeLayout.addView(petCanvas);
            petCanvas.setOnClickListener(v -> Log.e("pet", "PET TOUCHED"));

            Drawable drawable;
            if (wallpaper != null && wallpaper >= 0 && wallpaper < RoomActivity.wallpapers.length) {
                try {
                    drawable = AppCompatResources.getDrawable(main, RoomActivity.wallpapers[wallpaper]);
                } catch (Exception e) { return; }
            } else {
                drawable = AppCompatResources.getDrawable(main, RoomActivity.wallpapers[0]);
                ref.document("Room").update("wallpaper", 0);
            }
            wallpaperBG.setImageDrawable(drawable);
        });


        // room button
        Button roomButton = view.findViewById(R.id.pet_room_button);
        roomButton.setOnClickListener(v -> {
            Intent intent = new Intent(main, RoomActivity.class);
            intent.putExtra(Pet.parcelKey, pet);
            startActivity(intent);
        });


        // edit pet button
        Button editButton = view.findViewById(R.id.pet_edit_button);
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(this.main, EditPetActivity.class);
            intent.putExtra(Pet.parcelKey, pet);
            intent.putExtra("XP", main.getXP());
            startActivity(intent);
        });


        // online Button
        Button onlineButton = view.findViewById(R.id.pet_online_button);
        onlineButton.setOnClickListener(v -> startActivity(new Intent(this.main, OnlineActivity.class)));

        return view;
    }
}
