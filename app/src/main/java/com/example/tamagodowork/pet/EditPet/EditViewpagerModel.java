package com.example.tamagodowork.pet.EditPet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.tamagodowork.R;

import java.util.ArrayList;

import java.util.List;

public class EditViewpagerModel {

    public static final int COLOUR = 0;
    public static final int HEAD = 1;
    public static final int EYES = 2;
    public static final int BODY = 3;

    private final int type;
    private Context context;
    public int[] content;

    public EditViewpagerModel(int type, Context context) {
        this.type = type;
        this.context = context;
        setContent();
    }

    public int getType() {
        return type;
    }

    private void setContent() {
        switch (this.type) {
            case COLOUR:
                this.content = new int[] {
                        R.color.egg_beige,
                        R.color.yellow,
                        R.color.blue,
                        R.color.peach,
                        R.color.teal_200,
                        R.color.grey,
                        R.color.purple_200
                };
                break;
            case HEAD:
                this.content = new int[] {
                        R.mipmap.c_head_1,
                        R.mipmap.acc_head_crown,
                        R.mipmap.c_head_1,
                        R.mipmap.c_head_1,
                        R.mipmap.c_head_1,
                        R.mipmap.c_head_1,
                        R.mipmap.c_head_1,
                        R.mipmap.c_head_1,
                        R.mipmap.c_head_1,
                        R.mipmap.c_head_1,
                        R.mipmap.c_head_1,
                        R.mipmap.c_head_1,
                        R.mipmap.c_head_1,
                };
                break;
            case EYES:
                this.content = new int[] {
                        R.mipmap.c_eyes_1,
                        R.mipmap.acc_eyes_shades,
                        R.mipmap.c_eyes_1,
                        R.mipmap.acc_eyes_shades,
                        R.mipmap.c_eyes_1,
                        R.mipmap.acc_eyes_shades,
                        R.mipmap.c_eyes_1,
                        R.mipmap.acc_eyes_shades,
                        R.mipmap.c_eyes_1,
                        R.mipmap.acc_eyes_shades,
                        R.mipmap.c_eyes_1,
                        R.mipmap.acc_eyes_shades,
                };
                break;
            case BODY:
                this.content = new int[] {
                        R.mipmap.acc_body_chain,
                        R.mipmap.acc_body_chain,
                        R.mipmap.acc_body_chain,
                        R.mipmap.acc_body_chain,
                        R.mipmap.acc_body_chain,
                        R.mipmap.acc_body_chain,
                        R.mipmap.acc_body_chain,
                        R.mipmap.acc_body_chain,
                        R.mipmap.acc_body_chain,
                        R.mipmap.acc_body_chain,
                        R.mipmap.acc_body_chain,
                        R.mipmap.acc_body_chain
                };
                break;
            default:
                this.content = new int[] {
                        R.mipmap.c_head_1,
                        R.mipmap.c_eyes_1,
                        R.mipmap.c_head_1,
                        R.mipmap.c_eyes_1,
                        R.mipmap.c_head_1,
                        R.mipmap.c_eyes_1,
                        R.mipmap.c_head_1,
                        R.mipmap.c_eyes_1,
                        R.mipmap.c_head_1,
                        R.mipmap.c_eyes_1,
                        R.mipmap.c_head_1,
                        R.mipmap.c_eyes_1,
                        R.mipmap.c_head_1,
                        R.mipmap.c_eyes_1,
                        R.mipmap.c_head_1,
                        R.mipmap.c_eyes_1
                };
                break;
        }
    }
}
