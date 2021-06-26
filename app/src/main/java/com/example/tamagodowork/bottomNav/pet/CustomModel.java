package com.example.tamagodowork.bottomNav.pet;

import com.example.tamagodowork.R;

/**
 * Model for each Customisation accessory in the grid view
 */
public class CustomModel {

    private final Pet.custom custom;
    public int[] content;

    public CustomModel(Pet.custom custom) {
        this.custom = custom;
        setContent();
    }

    public Pet.custom getCustom() {
        return custom;
    }

    private void setContent() {
        switch (this.custom) {
            case COLOUR:
                this.content = new int[] {
                        R.color.egg_beige,
                        R.color.peach,
                        R.color.yellow,
                        R.color.green,
                        R.color.teal_200,
                        R.color.blue,
                        R.color.purple,
                        R.color.grey,
                };
                break;
            case HEAD:
                this.content = new int[] {
                        R.mipmap.none,
                        R.mipmap.c_head_1,
                        R.mipmap.acc_head_crown,
                        R.mipmap.acc_head_scar,
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
                break;
            case BODY:
                this.content = new int[] {
                        R.mipmap.none,
                        R.mipmap.acc_body_chain,
                        R.mipmap.acc_body_bow,
                        R.mipmap.acc_body_necklace,
                        R.mipmap.acc_body_tie,
                        R.mipmap.acc_body_chain,
                        R.mipmap.acc_body_chain,
                        R.mipmap.acc_body_chain,
                        R.mipmap.acc_body_chain,
                        R.mipmap.acc_body_chain,
                        R.mipmap.acc_body_chain
                };
                break;
            default:
                this.content = new int[] {};
                break;
        }
    }
}
