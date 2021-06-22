package com.example.tamagodowork.bottomNav.pet;

import android.content.Context;
import android.content.Intent;
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

import org.jetbrains.annotations.NotNull;

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

        View view = inflater.inflate(R.layout.pet_frag, container, false);

        // wallpaper
        ImageView wallpaperBG = view.findViewById(R.id.pet_frag_wallpaper);
        MainActivity.userDoc.collection("Pet").document("Room").get()
                .addOnSuccessListener(documentSnapshot -> {
                    wallpaper = documentSnapshot.get("wallpaper", Integer.class);
                    if (wallpaper != null && wallpaper != -1) {
                        wallpaperBG.setImageDrawable(AppCompatResources.getDrawable(
                                context, wallpaper));
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

        return view;
    }
}
