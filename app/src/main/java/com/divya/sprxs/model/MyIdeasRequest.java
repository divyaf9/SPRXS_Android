package com.divya.sprxs.model;

public class MyIdeasRequest {

    private String lkp_email;
    private String idea_name;
    private String idea_description;

    public MyIdeasRequest(String lkp_email, String idea_name, String idea_description) {
        this.lkp_email = lkp_email;
        this.idea_name = idea_name;
        this.idea_description = idea_description;
    }

    public String getLkp_email() {
        return lkp_email;
    }

    public String getIdea_name() {
        return idea_name;
    }

    public String getIdea_description() {
        return idea_description;
    }
}
