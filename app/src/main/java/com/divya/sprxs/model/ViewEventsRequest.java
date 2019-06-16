package com.divya.sprxs.model;

public class ViewEventsRequest {

    private int id;
    private String event_type;
    private String idea_id;
    private String event_description;
    private String created_profile_recipient;
    private String created_profile_sender;

    public ViewEventsRequest(int id, String event_type, String idea_id, String event_description, String created_profile_recipient, String created_profile_sender) {
        this.id = id;
        this.event_type = event_type;
        this.idea_id = idea_id;
        this.event_description = event_description;
        this.created_profile_recipient = created_profile_recipient;
        this.created_profile_sender = created_profile_sender;
    }
}
