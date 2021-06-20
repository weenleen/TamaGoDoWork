package com.example.tamagodowork.bottomNav.pet;

import androidx.annotation.NonNull;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;

import org.jetbrains.annotations.NotNull;

public class Pet {

    public enum custom {
        COLOUR, HEAD, EYES, BODY;

        @NonNull
        @NotNull
        @Override
        public String toString() {
            return super.toString().charAt(0) + super.toString().substring(1).toLowerCase();
        }
    }

    private Integer bodyColour = null;
    private Integer acc_head = null;
    private Integer acc_eyes = null;
    private Integer acc_body = null;

    // For current user's pet
    public Pet() {
        if (MainActivity.userDoc == null) return;

        MainActivity.userDoc.collection("Pet")
                .document("Customisation").get()
                .addOnSuccessListener(documentSnapshot -> {
                    this.acc_head = documentSnapshot.get("acc_head", Integer.class);
                    this.acc_eyes = documentSnapshot.get("acc_eyes", Integer.class);
                    this.acc_body = documentSnapshot.get("acc_body", Integer.class);

                    Integer tmp = documentSnapshot.get("bodyColour", Integer.class);
                    if (tmp != null) this.bodyColour = tmp;
                    else this.bodyColour = R.color.egg_beige;
        });
    }

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

    public Integer getAcc_head() {
        return acc_head;
    }

    public Integer getAcc_eyes() {
        return acc_eyes;
    }

    public Integer getAcc_body() {
        return acc_body;
    }
}
