package com.example.tamagodowork.bottomNav.pet.online;


import com.example.tamagodowork.bottomNav.pet.Pet;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class PetUser implements Comparable<PetUser> {

    private String id;
    private String name;
    private Pet pet = null;
    private final Integer xp;
    private boolean expanded;

    private final Task<DocumentSnapshot> task;

    public PetUser(String id, String name, Integer xp, DocumentReference userDoc) {
        this.id = id;
        this.name = name;
        this.xp = xp;
        this.expanded = false;
        this.task = userDoc.collection("Pet").document("Customisation").get()
                .addOnSuccessListener(
                        documentSnapshot -> this.pet = documentSnapshot.toObject(Pet.class));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return xp / 100 + 1;
    }

    public Pet getPet() { return pet; }

    public Task<DocumentSnapshot> getTask() { return this.task; }

    public boolean isExpanded() { return expanded; }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExpanded() { this.expanded = !expanded; }

    public void setPet(Pet pet) { this.pet = pet; }

    @Override
    public int compareTo(PetUser o) {
        if (this == o) return 0;
        else return this.xp.compareTo(o.xp);
    }
}
