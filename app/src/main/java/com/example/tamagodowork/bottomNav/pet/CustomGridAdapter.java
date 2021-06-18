package com.example.tamagodowork.bottomNav.pet;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.example.tamagodowork.R;

/**
 * Grid Adapter for each type of customisation
 */
public class CustomGridAdapter extends BaseAdapter {

    private final Context context;
    private final int[] arr;
    private final Pet.custom custom;

    public CustomGridAdapter(Context context, int[] arr, Pet.custom custom) {
        this.context = context;
        this.arr = arr;
        this.custom = custom;
    }

    @Override
    public int getCount() {
        return this.arr.length;
    }

    @Override
    public Object getItem(int position) {
        return this.arr[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) return convertView;

        ImageView imageView = new ImageView(this.context);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        if (this.custom == Pet.custom.COLOUR) { // COLOUR
            GradientDrawable tmp = (GradientDrawable) AppCompatResources
                    .getDrawable(context, R.drawable.button_color_picker);
            int colourId = this.arr[position];

            if (tmp != null) {
                tmp.setColor(ContextCompat.getColor(context, colourId));
                imageView.setImageDrawable(tmp);
            } else return imageView;

        } else { // SOMETHING ELSE
            imageView.setImageBitmap(
                    BitmapFactory.decodeResource(context.getResources(), this.arr[position]));
        }

        // on click
        imageView.setOnClickListener(v -> {
            if (EditPetActivity.petCanvas == null) return;
            EditPetActivity.petCanvas.setCustom(this.custom, this.arr[position]);
        });

        return imageView;
    }
}
