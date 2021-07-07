package com.example.tamagodowork.bottomNav.pet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EditPetActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ArrayList<CustomModel> lst;
    private int XP;

    public PetCanvas petCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_edit);

        this.XP = getIntent().getIntExtra("XP", 0);
        Log.e("edit pet activity", "XP " + this.XP);

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
        categoryName.setText(lst.get(viewPager.getCurrentItem()).getCustom().toString());
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                categoryName.setText(lst.get(viewPager.getCurrentItem()).getCustom().toString());
            }
        });

        // next button
        ImageButton nextButton = findViewById(R.id.edit_next_button);
        nextButton.setOnClickListener(v -> {
            int position = viewPager.getCurrentItem() + 1;
            if (position >= lst.size()) return;
            viewPager.setCurrentItem(position);
        });

        // done button
        Button doneButton = findViewById(R.id.edit_pet_done);
        doneButton.setOnClickListener(v -> {
            petCanvas.save();
            MainActivity.backToMain(EditPetActivity.this);
        });

        // cancel button
        Button cancelButton = findViewById(R.id.edit_pet_cancel);
        cancelButton.setOnClickListener(v -> {
            MainActivity.backToMain(EditPetActivity.this);
        });
    }

    // Handles all the views for the viewpager.
    private void loadCards() {
        this.lst = new ArrayList<>();

        // add all the stuff
        for (Pet.custom custom : Pet.custom.values()) {
            this.lst.add(new CustomModel(custom));
        }

        EditViewpagerAdapter adapter = new EditViewpagerAdapter();
        this.viewPager.setAdapter(adapter);
    }

    /**
     * adapter for the viewpager
     */
    public class EditViewpagerAdapter extends PagerAdapter {

        public EditViewpagerAdapter() { }

        @NonNull @NotNull @Override
        public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
            GridView gridView = new GridView(getApplicationContext());
            gridView.setNumColumns(3);
            gridView.setHorizontalSpacing(30);
            gridView.setVerticalSpacing(30);
            gridView.setDrawSelectorOnTop(true);
            CustomGridAdapter adapter = new CustomGridAdapter(EditPetActivity.this, lst.get(position), XP);
            gridView.setAdapter(adapter);

            container.addView(gridView, 0);
            return gridView;
        }

        @Override
        public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return lst.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
            return view.equals(object);
        }
    }

    public void setPetCanvas(Pet.custom custom, int id) {
        this.petCanvas.setCustom(custom, id);
    }
}