package com.example.tamagodowork.pet.EditPet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.tamagodowork.R;

import java.util.ArrayList;
import java.util.List;

public class EditGridAdapter extends BaseAdapter {

    private final Context context;
    private List<Bitmap> lst;

    private LayoutInflater inflater;

    public EditGridAdapter(Context context, List<Bitmap> lst) {
        this.context = context;
        this.lst = lst;
    }

    @Override
    public int getCount() {
        return this.lst.size();
    }

    @Override
    public Object getItem(int position) {
        return this.lst.get(position);
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
            convertView = inflater.inflate(R.layout.edit_pet_grid_item, null);
        }

        ImageView imageView = convertView.findViewById(R.id.grid_item_content);
        imageView.setImageBitmap(this.lst.get(position));

        return convertView;
    }
}
