package com.example.tamagodowork.bottomNav.pet;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;


import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.google.firebase.firestore.DocumentReference;

import org.jetbrains.annotations.NotNull;

import java.util.Map;


public class PetFrag extends Fragment {

    private Integer wallpaper;
    private Context context;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_pet, container, false);

        // wallpaper
        ImageView wallpaperBG = view.findViewById(R.id.pet_frag_wallpaper);
        MainActivity.userDoc.collection("Pet").document("Room").get()
                .addOnSuccessListener(documentSnapshot -> {
                    wallpaper = documentSnapshot.get("wallpaper", Integer.class);
                    if (wallpaper != null && wallpaper != -1) {
                        Drawable drawable;
                        try {
                            drawable = AppCompatResources.getDrawable(context, wallpaper);
                        } catch (Exception e) {
                            return;
                        }
                        wallpaperBG.setImageDrawable(drawable);
                    }
                });

        // pet
        RelativeLayout relativeLayout = view.findViewById(R.id.pet_area);
        PetCanvas petCanvas = new PetCanvas(context, new Pet());
        relativeLayout.addView(petCanvas);

        petCanvas.setOnClickListener(v -> Log.e("pet", "PET TOUCHED"));

        // room button
        Button roomButton = view.findViewById(R.id.pet_room_button);
        roomButton.setOnClickListener(v -> startActivity(new Intent(context, RoomActivity.class)));

        // edit pet button
        Button editButton = view.findViewById(R.id.pet_edit_button);
        editButton.setOnClickListener(v -> startActivity(new Intent(context, EditPetActivity.class)));

        TextView petName = view.findViewById(R.id.pet_name);

        EditText editPetName = view.findViewById(R.id.pet_name_edit);
        Button saveChanges = view.findViewById(R.id.name_change);
        DocumentReference ref = MainActivity.userDoc.collection("Pet").document("Name");
        saveChanges.setOnClickListener(v -> {
                    String name = editPetName.getText().toString();
                    if (!TextUtils.isEmpty(name)) {
                        ref.set(Map.of("name", name));
                        petName.setText(name);
                    }
                }
        );

        MainActivity.userDoc.collection("Pet").document("Name").addSnapshotListener(
                (value, error) -> {
                    if (error != null || value == null) return;

                    String tmp = value.get("name", String.class);
                    if (tmp == null) {
                        petName.setText("");
                        ref.update("name", "");
                        return;
                    }
                    petName.setText(tmp);
                });

        return view;
    }
}
