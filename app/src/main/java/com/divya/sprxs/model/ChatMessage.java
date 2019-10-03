package com.divya.sprxs.model;

public class ChatMessage {


    private String content;
    private String fromID;
    private Boolean isRead;
    private Long timestamp;
    private String toID;
    private String type;

    public ChatMessage() {
    }

    public ChatMessage(String content, String fromID, Boolean isRead, Long timestamp, String toID, String type) {
        this.content = content;
        this.fromID = fromID;
        this.isRead = isRead;
        this.timestamp = timestamp;
        this.toID = toID;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromID() {
        return fromID;
    }

    public void setFromID(String fromID) {
        this.fromID = fromID;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getToID() {
        return toID;
    }

    public void setToID(String toID) {
        this.toID = toID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
