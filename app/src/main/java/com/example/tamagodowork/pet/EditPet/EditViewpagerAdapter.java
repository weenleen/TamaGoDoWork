package com.example.tamagodowork.pet.EditPet;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.tamagodowork.R;
import com.example.tamagodowork.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EditViewpagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<EditViewpagerModel> lst;

    public EditViewpagerAdapter(Context context, ArrayList<EditViewpagerModel> lst) {
        this.context = context;
        this.lst = lst;
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.edit_pet_viewpager_item, container, false);

        GridView gridView = view.findViewById(R.id.viewpager_item_grid);
        EditGridAdapter adapter = new EditGridAdapter(context, this.lst.get(position).content);
        gridView.setAdapter(adapter);

        container.addView(view, position);

        return view;
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return this.lst.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view.equals(object);
    }
}
