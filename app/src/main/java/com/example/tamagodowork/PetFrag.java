package com.example.tamagodowork;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tamagodowork.pet.EditPet.EditPetActivity;
import com.example.tamagodowork.pet.PetCanvas;

public class PetFrag extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pet_frag, container, false);
        RelativeLayout relativeLayout = view.findViewById(R.id.pet_area);
        PetCanvas petCanvas = new PetCanvas(getContext());
        relativeLayout.addView(petCanvas);

        petCanvas.setOnClickListener(v -> Log.e("pet", "PET TOUCHED"));

        // edit pet button
        Button editButton = view.findViewById(R.id.pet_edit_button);
        editButton.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), EditPetActivity.class));
        });

        return view;
    }
}
