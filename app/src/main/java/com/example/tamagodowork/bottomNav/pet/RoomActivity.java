package com.example.tamagodowork.bottomNav.pet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.example.tamagodowork.authentication.RegisterAct;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static android.content.ContentValues.TAG;

public class RoomActivity extends AppCompatActivity {

    public final static int[] wallpapers = new int[] {
            R.drawable.wallpaper_default,
            R.drawable.wallpaper_space,
            R.drawable.wallpaper_snow,
            R.drawable.wallpaper_desert,
            R.drawable.wallpaper_exercise,
            R.drawable.wallpaper_ocean,
            R.drawable.wallpaper_gym };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_room);

        ViewPager viewPager = findViewById(R.id.room_viewpager);

        Pet pet = getIntent().getParcelableExtra(Pet.parcelKey);
        if (pet == null) pet = Pet.defaultPet();

        InfinitePagerAdapter adapter = new InfinitePagerAdapter(new RoomViewpagerAdapter(getApplicationContext(), wallpapers));
        viewPager.setAdapter(adapter);


        // show pet
        RelativeLayout petArea = findViewById(R.id.room_pet_area);
        PetCanvas petCanvas = new PetCanvas(getApplicationContext(), pet);
        petArea.addView(petCanvas);

        // prev button
        FloatingActionButton prevButton = findViewById(R.id.room_prev_button);
        prevButton.setOnClickListener(v -> {
            int position = viewPager.getCurrentItem() - 1;
            if (position < 0) position = wallpapers.length - 1;
            viewPager.setCurrentItem(position);
        });

        // next button
        FloatingActionButton nextButton = findViewById(R.id.room_next_button);
        nextButton.setOnClickListener(v -> {
            int position = viewPager.getCurrentItem() + 1;
            if (position >= wallpapers.length) position = 0;
            viewPager.setCurrentItem(position);
        });

        // done
        Button doneButton = findViewById(R.id.room_done);
        doneButton.setOnClickListener(v -> {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            // check if logged in
            if (firebaseAuth.getCurrentUser() == null) {
                startActivity(new Intent(RoomActivity.this, RegisterAct.class));
                finish(); return;
            }
            String userId = firebaseAuth.getCurrentUser().getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(userId).collection("Pet").document("Room")
                    .set(Map.of("wallpaper", viewPager.getCurrentItem() % wallpapers.length));

            MainActivity.backToMain(RoomActivity.this);
        });

        // cancel
        Button cancelButton = findViewById(R.id.room_cancel);
        cancelButton.setOnClickListener(v -> MainActivity.backToMain(RoomActivity.this));
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
                view.setImageDrawable(AppCompatResources.getDrawable(this.context, wallpapers[position]));
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


    /**
     * A PagerAdapter that wraps around another PagerAdapter to handle paging wrap-around.
     * INFINITE SCROLLING
     */
    public static class InfinitePagerAdapter extends PagerAdapter {

        private static final boolean DEBUG = false;

        private final PagerAdapter adapter;

        public InfinitePagerAdapter(PagerAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public int getCount() {
            if (getRealCount() == 0) {
                return 0;
            }
            // reduce the page scroll to 500 to reduce lag
            return 500;
        }

        // get the real count of the outer adapter - infinite page adapter
        public int getRealCount() {
            return adapter.getCount();
        }

        @NotNull
        @Override
        public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
            int virtualPosition = position % getRealCount();
            debug("instantiateItem: real position: " + position);
            debug("instantiateItem: virtual position: " + virtualPosition);

            // only expose virtual position to the inner adapter
            return adapter.instantiateItem(container, virtualPosition);
        }

        @Override
        public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
            int virtualPosition = position % getRealCount();
            debug("destroyItem: real position: " + position);
            debug("destroyItem: virtual position: " + virtualPosition);
            adapter.destroyItem(container, virtualPosition, object);
        }

        @Override
        public void finishUpdate(@NotNull ViewGroup container) {
            adapter.finishUpdate(container);
        }

        @Override
        public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
            return adapter.isViewFromObject(view, object);
        }

        @Override
        public void restoreState(Parcelable bundle, ClassLoader classLoader) {
            adapter.restoreState(bundle, classLoader);
        }

        @Override
        public Parcelable saveState() {
            return adapter.saveState();
        }

        @Override
        public void startUpdate(@NotNull ViewGroup container) {
            adapter.startUpdate(container);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            int virtualPosition = position % getRealCount();
            return adapter.getPageTitle(virtualPosition);
        }

        @Override
        public float getPageWidth(int position) {
            return adapter.getPageWidth(position);
        }

        @Override
        public void setPrimaryItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
            adapter.setPrimaryItem(container, position, object);
        }

        @Override
        public void unregisterDataSetObserver(@NotNull DataSetObserver observer) {
            adapter.unregisterDataSetObserver(observer);
        }

        @Override
        public void registerDataSetObserver(@NotNull DataSetObserver observer) {
            adapter.registerDataSetObserver(observer);
        }

        @Override
        public void notifyDataSetChanged() {
            adapter.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(@NotNull Object object) {
            return adapter.getItemPosition(object);
        }

        private void debug(String message) {
            if (DEBUG) {
                Log.d(TAG, message);
            }
        }
    }
}