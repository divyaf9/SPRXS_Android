package com.divya.sprxs.model;

public class ChatDetails {

    private String name;

    private ChatMessage chatMessage;

    public ChatDetails() {
    }

    public ChatDetails(String name, ChatMessage chatMessage) {
        this.name = name;
        this.chatMessage = chatMessage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }
}
