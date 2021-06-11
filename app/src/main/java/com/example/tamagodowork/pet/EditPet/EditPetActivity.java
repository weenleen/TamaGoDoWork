package com.example.tamagodowork.pet.EditPet;

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
import com.example.tamagodowork.pet.PetCanvas;

import java.util.ArrayList;

public class EditPetActivity extends AppCompatActivity {

    private static String[] categories = new String[] {
            "COLOUR",
            "HEAD",
            "EYES"
    };

    private ViewPager viewPager;
    private ArrayList<EditViewpagerModel> lst;
    private EditViewpagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pet);

        // view pager stuff
        viewPager = findViewById(R.id.edit_pet_viewpager);
        loadCards();

        // show the pet
        RelativeLayout petArea = findViewById(R.id.edit_pet_area);
        PetCanvas petCanvas = new PetCanvas(getApplicationContext());
        petArea.addView(petCanvas);

        // prev button
        ImageButton prevButton = findViewById(R.id.edit_prev_button);
        prevButton.setOnClickListener(v -> {
            int position = viewPager.getCurrentItem() - 1;
            if (position < 0) return;
            viewPager.setCurrentItem(position);
        });

        // category name
        TextView categoryName = findViewById(R.id.edit_content_name);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                categoryName.setText(categories[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        // next button
        ImageButton nextButton = findViewById(R.id.edit_next_button);
        nextButton.setOnClickListener(v -> {
            int position = viewPager.getCurrentItem() + 1;
            if (position > viewPager.getChildCount() - 1) return;
            viewPager.setCurrentItem(position);
        });

        // done button
        Button doneButton = findViewById(R.id.edit_pet_done);
        doneButton.setOnClickListener(v -> {
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
        for (int i = 0; i < categories.length; i++) {
            this.lst.add(new EditViewpagerModel(i, getApplicationContext()));
        }

        this.adapter = new EditViewpagerAdapter(getApplicationContext(), this.lst);
        this.viewPager.setAdapter(adapter);
    }
}