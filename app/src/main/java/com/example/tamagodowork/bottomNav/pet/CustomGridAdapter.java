package com.example.tamagodowork.bottomNav.pet;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private final int userLevel;

    public CustomGridAdapter(FragmentActivity act, CustomModel model, int userXP) {
        this.act = act;
        this.context = act;
        this.arr = model.getContent();
        this.custom = model.getCustom();
        this.userLevel = userXP / 100 + 1;
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
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            view = inflater.inflate(R.layout.pet_custom_grid_item, parent, false);
        }

        ImageView imageView = view.findViewById(R.id.custom_item_imageView);
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


        LinearLayout unlockLayout = view.findViewById(R.id.unlock_layout);
        if (unlocked(position)) {
            view.setOnClickListener(v -> {
                if (!(this.act instanceof EditPetActivity)) return;
                ((EditPetActivity)act).setPetCanvas(this.custom, this.arr[position]);
            });
            unlockLayout.setVisibility(View.GONE);
        } else {
            TextView txtView = view.findViewById(R.id.unlock_level_text);
            int unlockLevel = getUnlockLevel(position);
            txtView.setText(context.getString(R.string.unlock_level, unlockLevel));

            final AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
            dialog.setMessage(context.getString(R.string.unlock_message, unlockLevel));
            view.setOnClickListener(v -> dialog.show());
        }
        return view;
    }

    /**
     * Determines if an item is unlocked based on the user's XP.
     *
     * @param position The position of the item in the grid.
     * @return Whether an item is unlocked.
     */
    private boolean unlocked(int position) {
        return getUnlockLevel(position) <= userLevel;
    }

    /**
     * Determines what level an item is unlocked.
     * The first 3 items are always unlocked.
     *
     * @param position The position of the item in the grid.
     * @return The level at which the item is unlocked.
     */
    private int getUnlockLevel(int position) {
        return (position - 2) * 2;
    }
}
