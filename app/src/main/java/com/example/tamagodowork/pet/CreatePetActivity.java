package com.example.tamagodowork.pet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.backToMain(CreatePetActivity.this);
                finish();
            }
        });

        ImageButton beigeButton = findViewById(R.id.beige_button);
        beigeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petCanvas.setBodyColour(R.color.egg_beige);
            }
        });

        ImageButton yellowButton = findViewById(R.id.yellow_button);
        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petCanvas.setBodyColour(R.color.yellow);
            }
        });

        ImageButton blueButton = findViewById(R.id.blue_button);
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petCanvas.setBodyColour(R.color.blue);
            }
        });
    }
}