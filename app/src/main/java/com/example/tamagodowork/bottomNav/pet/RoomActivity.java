package com.example.tamagodowork.bottomNav.pet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class RoomActivity extends AppCompatActivity {

    private int[] wallpapers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        ViewPager viewPager = findViewById(R.id.room_viewpager);
        loadWallpapers();

        RoomViewpagerAdapter adapter = new RoomViewpagerAdapter(getApplicationContext(), wallpapers);
        viewPager.setAdapter(adapter);


        // show pet
        RelativeLayout petArea = findViewById(R.id.room_pet_area);
        PetCanvas petCanvas = new PetCanvas(getApplicationContext(), new Pet());
        petArea.addView(petCanvas);

        // prev button
        ImageButton prevButton = findViewById(R.id.room_prev_button);
        prevButton.setOnClickListener(v -> {
            int position = viewPager.getCurrentItem() - 1;
            if (position < 0) position = wallpapers.length - 1;
            viewPager.setCurrentItem(position);
        });

        // next button
        ImageButton nextButton = findViewById(R.id.room_next_button);
        nextButton.setOnClickListener(v -> {
            int position = viewPager.getCurrentItem() + 1;
            if (position >= wallpapers.length) position = 0;
            viewPager.setCurrentItem(position);
        });

        // done
        Button doneButton = findViewById(R.id.room_done);
        doneButton.setOnClickListener(v -> {
            MainActivity.userDoc.collection("Pet").document("Room").set(
                    Map.of("wallpaper", wallpapers[viewPager.getCurrentItem()])
            );

            startActivity(new Intent(RoomActivity.this, MainActivity.class));
            finish();
        });

        // cancel
        Button cancelButton = findViewById(R.id.room_cancel);
        cancelButton.setOnClickListener(v -> {
            startActivity(new Intent(RoomActivity.this, MainActivity.class));
            finish();
        });
    }


    /**
     * Add all wallpapers here.
     */
    private void loadWallpapers() {
        wallpapers = new int[] {
                -1,
                R.drawable.wallpaper_blue,
                R.drawable.wallpaper_orange,
                R.drawable.wallpaper_purple,
                R.drawable.exercise
        };
    }


    /**
     * Adapter for the viewpager.
     */
    private static class RoomViewpagerAdapter extends PagerAdapter {

        private final Context context;
        private final int[] wallpapers;

        public RoomViewpagerAdapter(Context context, int[] lst) {
            this.context = context;
            this.wallpapers = lst;
        }

        @NonNull
        @NotNull
        @Override
        public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
            ImageView view = new ImageView(this.context);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (wallpapers[position] != -1) {
                view.setImageDrawable(
                        AppCompatResources.getDrawable(this.context, wallpapers[position]));
            }
            container.addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return this.wallpapers.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
            return view.equals(object);
        }
    }
}