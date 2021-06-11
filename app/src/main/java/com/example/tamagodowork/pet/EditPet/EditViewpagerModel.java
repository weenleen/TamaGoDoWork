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
    public static final int EYE = 2;

    private final int type;
    private Context context;
    public List<Bitmap> content;

    public EditViewpagerModel(int type, Context context) {
        this.type = type;
        this.context = context;

        this.content = new ArrayList<>();
        this.content.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.c_head_1));
        this.content.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.c_eyes_1));
        this.content.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.c_head_1));
        this.content.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.c_eyes_1));
        this.content.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.c_head_1));
        this.content.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.c_eyes_1));
        this.content.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.c_head_1));
        this.content.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.c_eyes_1));
        this.content.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.c_head_1));
        this.content.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.c_eyes_1));
        this.content.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.c_head_1));
        this.content.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.c_eyes_1));
        this.content.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.c_head_1));
        this.content.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.c_eyes_1));
        this.content.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.c_head_1));
        this.content.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.c_eyes_1));
    }

    public int getType() {
        return type;
    }

//    private void setContent() {
//        this.content = new ArrayList<>();
//        switch (this.type) {
//            case HEAD:
//                this.content.add(
//                        BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.c_head_1));
//                break;
//            case EYE:
//                this.content.add(
//                        BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.c_eyes_1));
//                break;
//            default:
//                this.content.add(
//                        BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.c_head_1));
//                break;
//        }
//    }
}
