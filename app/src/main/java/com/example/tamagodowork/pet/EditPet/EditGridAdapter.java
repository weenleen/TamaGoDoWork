package com.example.tamagodowork.pet.EditPet;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.example.tamagodowork.R;

public class EditGridAdapter extends BaseAdapter {

    private final Context context;
    private final int[] arr;
    private final int type;

    private LayoutInflater inflater;

    public EditGridAdapter(Context context, int[] arr, int type) {
        this.context = context;
        this.arr = arr;
        this.type = type;
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

        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.edit_pet_grid_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.grid_item_content);

        if (this.type == 0) { // COLOUR
            GradientDrawable tmp = (GradientDrawable) AppCompatResources
                    .getDrawable(context, R.drawable.button_color_picker);
            int colourId = this.arr[position];
            if (tmp == null) return convertView;
            tmp.setColor(ContextCompat.getColor(context, colourId));
            imageView.setImageDrawable(tmp);

            // on click
            convertView.setOnClickListener(v -> {
                if (EditPetActivity.petCanvas == null) return;
                EditPetActivity.petCanvas.setBodyColour(colourId);
            });

        } else {
            imageView.setImageBitmap(
                    BitmapFactory.decodeResource(context.getResources(), this.arr[position]));

            // on click
            convertView.setOnClickListener(v -> {
                if (EditPetActivity.petCanvas == null) return;
                EditPetActivity.petCanvas.set(this.type, this.arr[position]);
            });
        }

        return convertView;
    }
}
