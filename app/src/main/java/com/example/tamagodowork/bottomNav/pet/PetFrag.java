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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;


public class PetFrag extends Fragment {

    private Integer wallpaper;
    private MainActivity main;

    private Pet pet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.main = (MainActivity) getActivity();
        if (main == null) return null;

        View view = inflater.inflate(R.layout.frag_pet, container, false);



        //  getting data
        ImageView wallpaperBG = view.findViewById(R.id.pet_frag_wallpaper);

        TextView petName = view.findViewById(R.id.pet_name);
        CollectionReference ref = MainActivity.userDoc.collection("Pet");

        ref.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) return;
            else if (task.getResult() == null) return;

            List<DocumentSnapshot> documents = task.getResult().getDocuments();

            for (DocumentSnapshot snapshot: documents) {
                String id = snapshot.getId();
                switch(id) {
                    case "Room": {
                        wallpaper = snapshot.get("wallpaper", Integer.class);
                        if (wallpaper != null && wallpaper != -1) {
                            Drawable drawable;
                            try {
                                drawable = AppCompatResources.getDrawable(main, wallpaper);
                            } catch (Exception e) { return; }
                            wallpaperBG.setImageDrawable(drawable);
                        }
                        break;
                    }
                    case "Customisation": {
                        pet = snapshot.toObject(Pet.class); break;
                    }
                    case "Name": {
                        String tmp = snapshot.get("name", String.class);
                        if (tmp == null) {
                            tmp = "";
                            ref.document("Name").update("name", tmp);
                        }
                        petName.setText(tmp);
                        break;
                    }
                }
            }

            if (pet == null) {
                pet = Pet.defaultPet();
                ref.document("Customisation").set(pet);
            }
            RelativeLayout relativeLayout = view.findViewById(R.id.pet_area);
            PetCanvas petCanvas = new PetCanvas(main, pet);
            relativeLayout.addView(petCanvas);
            petCanvas.setOnClickListener(v -> Log.e("pet", "PET TOUCHED"));
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
            Intent intent = new Intent(main, EditPetActivity.class);
            intent.putExtra(Pet.parcelKey, pet);
            intent.putExtra("XP", main.getXP());
            startActivity(intent);
        });


        // online Button
        Button onlineButton = view.findViewById(R.id.pet_online_button);
        onlineButton.setOnClickListener(v -> {

        });

        return view;
    }
}
