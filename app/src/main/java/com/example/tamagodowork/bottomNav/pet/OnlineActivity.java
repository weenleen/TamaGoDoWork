package com.example.tamagodowork.bottomNav.pet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tamagodowork.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OnlineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
    }


    /**
     * Adapter for the viewpager.
     */
    private static class OnlineViewpagerAdapter extends PagerAdapter {

        private final Context context;
        private final List<petUser> users;

        public OnlineViewpagerAdapter(Context context, List<petUser> lst) {
            this.context = context;
            this.users = lst;
        }

        @NonNull @NotNull @Override
        public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {

            View view =  LayoutInflater.from(this.context).inflate(R.layout.online_viewpager_user, container, false);
            petUser user = users.get(position);

            RelativeLayout petArea = view.findViewById(R.id.online_pet_area);
            petArea.addView(new PetCanvas(context, user.getPet()));

            TextView userDetails = view.findViewById(R.id.online_user_text);

            ImageView wallpaper = view.findViewById(R.id.online_user_wallpaper);

            return view;
        }

        @Override
        public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return this.users.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
            return view.equals(object);
        }
    }
}