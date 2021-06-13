package com.example.tamagodowork.bottomNav.pet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;

import java.util.ArrayList;

public class EditPetActivity extends AppCompatActivity {

    private static final String[] categories = new String[] {
            "Colours",
            "Head",
            "Eyes",
            "Body"
    };

    private ViewPager viewPager;
    private ArrayList<CustomModel> lst;
    private EditViewpagerAdapter adapter;

    public static PetCanvas petCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pet);

        // view pager stuff
        viewPager = findViewById(R.id.edit_pet_viewpager);
        loadCards();

        // show the pet
        RelativeLayout petArea = findViewById(R.id.edit_pet_area);
        petCanvas = new PetCanvas(getApplicationContext(), new Pet());
        petArea.addView(petCanvas);

        // prev button
        ImageButton prevButton = findViewById(R.id.edit_prev_button);
        prevButton.setOnClickListener(v -> {
            int position = viewPager.getCurrentItem() - 1;
            if (position < 0) return;
            viewPager.setCurrentItem(position);
        });

        // category name text
        TextView categoryName = findViewById(R.id.edit_content_name);
        categoryName.setText(categories[viewPager.getCurrentItem()]);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                categoryName.setText(categories[position]);
            }
        });

        // next button
        ImageButton nextButton = findViewById(R.id.edit_next_button);
        nextButton.setOnClickListener(v -> {
            int position = viewPager.getCurrentItem() + 1;
            if (position >= categories.length) return;
            viewPager.setCurrentItem(position);
        });

        // done button
        Button doneButton = findViewById(R.id.edit_pet_done);
        doneButton.setOnClickListener(v -> {
            petCanvas.save();
            startActivity(new Intent(EditPetActivity.this, MainActivity.class));
            finish();
        });

        // cancel button
        Button cancelButton = findViewById(R.id.edit_pet_cancel);
        cancelButton.setOnClickListener(v -> {
            startActivity(new Intent(EditPetActivity.this, MainActivity.class));
            finish();
        });
    }

    /**
     * Handles all the views for the viewpager.
     */
    private void loadCards() {
        this.lst = new ArrayList<>();

        // add all the stuff
        for (Pet.custom custom : Pet.custom.values()) {
            this.lst.add(new CustomModel(custom, getApplicationContext()));
        }

        this.adapter = new EditViewpagerAdapter(getApplicationContext(), this.lst);
        this.viewPager.setAdapter(adapter);
    }
}