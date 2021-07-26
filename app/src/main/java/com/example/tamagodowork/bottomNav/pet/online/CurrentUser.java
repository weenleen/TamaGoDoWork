package com.example.tamagodowork.bottomNav.pet.online;

import java.util.ArrayList;
import java.util.List;

public class CurrentUser {
    @SuppressWarnings({"UnusedDeclaration"})
    private List<String> friendsList;
    @SuppressWarnings({"UnusedDeclaration"})
    private List<String> sentRequests;
    @SuppressWarnings({"UnusedDeclaration"})
    private List<String> receivedRequests;

    public CurrentUser() {}

    public List<String> getFriendsList() {
        if (friendsList == null) friendsList = new ArrayList<>();
        return friendsList;
    }

    public List<String> getSentRequests() {
        if (sentRequests == null) sentRequests = new ArrayList<>();
        return sentRequests;
    }

    public List<String> getReceivedRequests() {
        if (receivedRequests == null) receivedRequests = new ArrayList<>();
        return receivedRequests;
    }
}
