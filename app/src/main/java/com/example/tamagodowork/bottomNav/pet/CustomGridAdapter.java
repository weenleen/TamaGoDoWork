package com.example.tamagodowork.bottomNav.pet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.tamagodowork.R;

/**
 * Grid Adapter for each type of customisation
 */
public class CustomGridAdapter extends BaseAdapter {

    private final FragmentActivity act;

    private final Context context;
    private final int[] arr;
    private final Pet.custom custom;

    public CustomGridAdapter(FragmentActivity act, CustomModel model) {
        this.act = act;
        this.context = act;
        this.arr = model.getContent();
        this.custom = model.getCustom();
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
            // code for the head and the necklace
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), this.arr[position]);
            // allows for resize of the way the bitmap appears on the selection option
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
            imageView.setImageBitmap(resizedBitmap);
        }

        // on click
        imageView.setOnClickListener(v -> {
            if (!(this.act instanceof EditPetActivity)) return;
            ((EditPetActivity)act).setPetCanvas(this.custom, this.arr[position]);
        });

        return imageView;
    }
}
