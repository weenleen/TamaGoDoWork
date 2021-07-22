package com.example.tamagodowork.bottomNav.pet.online;

import java.util.List;

public class CurrentUser {
    @SuppressWarnings({"UnusedDeclaration"})
    private List<String> friendsList;
    @SuppressWarnings({"UnusedDeclaration"})
    private List<String> sentRequests;
    @SuppressWarnings({"UnusedDeclaration"})
    private List<String> receivedRequests;

    public CurrentUser() {}

    public List<String> getFriendsList() { return friendsList; }

    public List<String> getSentRequests() { return sentRequests; }

    public List<String> getReceivedRequests() { return receivedRequests; }
}
