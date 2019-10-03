package com.divya.sprxs.model;

import java.util.List;

public class ChatUserDetails {

    private String fullName;

    private String collabFirebaseUID;

    private List<String> chatMessage;

    public ChatUserDetails() {
    }

    public ChatUserDetails(String fullName, String collabFirebaseUID, List<String> chatMessage) {
        this.fullName = fullName;
        this.collabFirebaseUID = collabFirebaseUID;
        this.chatMessage = chatMessage;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCollabFirebaseUID() {
        return collabFirebaseUID;
    }

    public void setCollabFirebaseUID(String collabFirebaseUID) {
        this.collabFirebaseUID = collabFirebaseUID;
    }

    public List<String> getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(List<String> chatMessage) {
        this.chatMessage = chatMessage;
    }
}
