package com.example.tamagodowork.bottomNav.pet.online;


public class PetUser {

    private String id;
    private String name;
    private final Integer xp;
    private boolean expanded;

    public PetUser(String id, String name, Integer xp) {
        this.id = id;
        this.name = name;
        this.xp = xp;
        this.expanded = false;
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

    public boolean isExpanded() { return expanded; }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExpanded() { this.expanded = !expanded; }
}
