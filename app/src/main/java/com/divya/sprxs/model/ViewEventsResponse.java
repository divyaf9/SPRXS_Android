package com.divya.sprxs.model;

public class ViewEventsResponse {

    private  int Event_type;
    private String firstname;
    private String surname;
    private String created_profile_recipient;
    private String date_created;
    private String event_description;
    private String description;
    private String id;
    private String Idea_id;
    private String title;
    private String created_profile_sender;
    private String error;

    public ViewEventsResponse(int event_type, String firstname, String surname, String created_profile_recipient, String date_created, String event_description, String description, String id, String idea_id, String title, String created_profile_sender, String error) {
        Event_type = event_type;
        this.firstname = firstname;
        this.surname = surname;
        this.created_profile_recipient = created_profile_recipient;
        this.date_created = date_created;
        this.event_description = event_description;
        this.description = description;
        this.id = id;
        Idea_id = idea_id;
        this.title = title;
        this.created_profile_sender = created_profile_sender;
        this.error = error;
    }

    public int getEvent_type() {
        return Event_type;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public String getCreated_profile_recipient() {
        return created_profile_recipient;
    }

    public String getDate_created() {
        return date_created;
    }

    public String getEvent_description() {
        return event_description;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getIdea_id() {
        return Idea_id;
    }

    public String getTitle() {
        return title;
    }

    public String getCreated_profile_sender() {
        return created_profile_sender;
    }

    public String getError() {
        return error;
    }
}
