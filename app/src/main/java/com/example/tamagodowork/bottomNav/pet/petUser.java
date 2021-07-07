package com.example.tamagodowork.bottomNav.pet;

public class petUser {

    private String id;
    private Pet pet;
    private int wallpaper;

    public petUser(String id, Pet pet, int wallpaper) {
        this.id = id;
        this.pet = pet;
        this.wallpaper = wallpaper;
    }

    public String getId() { return id; }

    public Pet getPet() { return pet; }

    public int getWallpaper() { return wallpaper; }
}
