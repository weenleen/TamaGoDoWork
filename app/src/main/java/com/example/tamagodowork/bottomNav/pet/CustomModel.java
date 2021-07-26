package com.example.tamagodowork.bottomNav.pet;

import com.example.tamagodowork.R;

/**
 * Model for each Customisation accessory in the grid view
 */
public class CustomModel {

    private static final int[] colour = new int[] {
            R.color.egg_beige,
            R.color.peach,
            R.color.yellow,
            R.color.green,
            R.color.teal_200,
            R.color.blue,
            R.color.purple,
            R.color.grey,
    };

    private static final int[] head = new int[] {
            R.mipmap.none,
            R.mipmap.c_head_1,
            R.mipmap.acc_head_crown,
            R.mipmap.acc_head_cat,
            R.mipmap.acc_head_graduationhat,
            R.mipmap.acc_head_buckethat,
            R.mipmap.acc_head_flowercrown,
            R.mipmap.acc_head_xmas,
            R.mipmap.c_head_1,
            R.mipmap.c_head_1,
            R.mipmap.c_head_1,
    };

    private static final int[] eyes = new int[] {
            R.mipmap.none,
            R.mipmap.acc_eyes_nerdy,
            R.mipmap.acc_eyes_shades,
            R.mipmap.acc_eyes_gcp,
            R.mipmap.acc_eyes_superman,
            R.mipmap.c_eyes_1,
            R.mipmap.acc_eyes_eyemask,
            R.mipmap.acc_eyes_clown,
            R.mipmap.acc_eyes_scientist,
            R.mipmap.acc_eyes_googly,
            R.mipmap.acc_eyes_anime,
    };

    private static final int[] body = new int[] {
            R.mipmap.none,
            R.mipmap.acc_body_chain,
            R.mipmap.acc_body_bow,
            R.mipmap.acc_body_necklace,
            R.mipmap.acc_body_tie,
            R.mipmap.acc_body_pearls,
            R.mipmap.acc_body_skarf,
            R.mipmap.acc_body_chain,
            R.mipmap.acc_body_chain,
            R.mipmap.acc_body_chain,
            R.mipmap.acc_body_chain,
            R.mipmap.acc_body_chain
    };

    private final Pet.custom custom;
    private int[] content;

    public CustomModel(Pet.custom custom) {
        this.custom = custom;
        setContent();
    }

    public Pet.custom getCustom() { return custom; }

    public int[] getContent() { return content; }

    private void setContent() {
        switch (this.custom) {
            case COLOUR: this.content = colour; break;
            case HEAD: this.content = head; break;
            case EYES: this.content = eyes; break;
            case BODY: this.content = body;break;
            default: this.content = new int[] {}; break;
        }
    }
}
