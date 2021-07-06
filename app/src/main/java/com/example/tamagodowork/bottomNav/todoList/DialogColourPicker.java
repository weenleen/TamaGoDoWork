package com.example.tamagodowork.bottomNav.todoList;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.tamagodowork.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DialogColourPicker extends BottomSheetDialogFragment {

    public static final String TAG = "Colour Picker Dialog";

    private final Context context;
    private final ImageButton initButton;

    public DialogColourPicker(Context context, ImageButton initButton) {
        this.context = context;
        this.initButton = initButton;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        final View dialogView = View.inflate(context, R.layout.dial_colour_picker, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(context);

        LinearLayout layout = dialogView.findViewById(R.id.colourPicker_image_layout);

        List<String> keyLst = new ArrayList<>(Todo.colourMap.keySet());

        for (int i = 0; i < Integer.min(layout.getChildCount(), keyLst.size()); i++) {

            ImageView imageView = (ImageView) layout.getChildAt(i);
            GradientDrawable tmp = (GradientDrawable) AppCompatResources
                    .getDrawable(context, R.drawable.button_color_picker);

            String selectedKey = keyLst.get(i);
            Integer colourId = Todo.colourMap.get(selectedKey);
            if (colourId == null) continue;

            if (tmp != null) {
                tmp.setColor(ContextCompat.getColor(context, colourId));
                imageView.setImageDrawable(tmp);
            }

            imageView.setOnClickListener(v12 -> {
                ((GradientDrawable) initButton.getDrawable()).setColor(
                        ContextCompat.getColor(context, colourId));

                FragmentActivity act = getActivity();

                if (act instanceof AddTodoActivity) {
                    ((AddTodoActivity) act).setColourKey(selectedKey);
                } else if (act instanceof EditTodoActivity) {
                    ((EditTodoActivity) act).setColourKey(selectedKey);
                }

                dialog.dismiss();
            });
        }

        dialog.setContentView(dialogView);
        return dialog;
    }
}