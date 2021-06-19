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
                        R.mipmap.none,
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
                        R.mipmap.none,
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
                        R.mipmap.none,
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
                        R.mipmap.none,
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
