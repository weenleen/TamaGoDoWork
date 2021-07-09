package com.example.tamagodowork.bottomNav.pet.online;


public class PetUser {

    private String id;
    private String name;
    private Integer xp;

    public PetUser(String id, String name, Integer xp) {
        this.id = id;
        this.name = name;
        this.xp = xp;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getXp() {
        return xp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }
}
