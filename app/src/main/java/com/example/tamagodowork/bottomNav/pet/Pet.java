package com.example.tamagodowork.bottomNav.pet;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.tamagodowork.R;

import org.jetbrains.annotations.NotNull;


public class Pet implements Parcelable {

    public static final String parcelKey = "PET PARCEL KEY";

    public enum custom {
        COLOUR, HEAD, EYES, BODY;

        @NonNull @NotNull @Override
        public String toString() {
            return super.toString().charAt(0) + super.toString().substring(1).toLowerCase();
        }
    }

    private Integer bodyColour = null;
    private Integer acc_head = null;
    private Integer acc_eyes = null;
    private Integer acc_body = null;

    protected Pet(Parcel in) {
        if (in.readByte() == 0) bodyColour = null;
        else bodyColour = in.readInt();

        if (in.readByte() == 0) acc_head = null;
        else acc_head = in.readInt();

        if (in.readByte() == 0) acc_eyes = null;
        else acc_eyes = in.readInt();

        if (in.readByte() == 0) acc_body = null;
        else acc_body = in.readInt();
    }

    public static final Creator<Pet> CREATOR = new Creator<Pet>() {
        @Override
        public Pet createFromParcel(Parcel in) { return new Pet(in); }

        @Override
        public Pet[] newArray(int size) { return new Pet[size]; }
    };

    public static Pet defaultPet() {
        return new Pet(R.color.egg_beige, null, null, null);
    }

    public Pet() {}

    public Pet(int bodyColour, Integer acc_head, Integer acc_eyes, Integer acc_body) {
        this.bodyColour = bodyColour;
        this.acc_head = acc_head;
        this.acc_eyes = acc_eyes;
        this.acc_body = acc_body;
    }

    // for other people's pet
    public Pet(String userId) {}

    // getters
    public Integer getBodyColour() { return bodyColour; }

    public Integer getAcc_head() { return acc_head; }

    public Integer getAcc_eyes() { return acc_eyes; }

    public Integer getAcc_body() {
        return acc_body;
    }



    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (bodyColour == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(bodyColour);
        }
        if (acc_head == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(acc_head);
        }
        if (acc_eyes == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(acc_eyes);
        }
        if (acc_body == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(acc_body);
        }
    }
}
