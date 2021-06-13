package com.example.tamagodowork.bottomNav.pet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;

public class CreatePetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pet);

        RelativeLayout petArea = findViewById(R.id.create_pet_area);
        PetCanvas petCanvas = new PetCanvas(getApplicationContext());
        petArea.addView(petCanvas);

        Button createButton = findViewById(R.id.create_pet_button);
        createButton.setOnClickListener(v -> {
            MainActivity.backToMain(CreatePetActivity.this);
            finish();
        });

        ImageButton beigeButton = findViewById(R.id.beige_button);
        beigeButton.setOnClickListener(v -> petCanvas.setBodyColour(R.color.egg_beige));

        ImageButton yellowButton = findViewById(R.id.yellow_button);
        yellowButton.setOnClickListener(v -> petCanvas.setBodyColour(R.color.yellow));

        ImageButton blueButton = findViewById(R.id.blue_button);
        blueButton.setOnClickListener(v -> petCanvas.setBodyColour(R.color.blue));
    }
}