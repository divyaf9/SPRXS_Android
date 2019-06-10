package com.divya.sprxs.model;

public class CreateIdeasRequest {

    private int lkp_idea_cat1;
    private int lkp_idea_cat2;
    private int lkp_idea_cat3;
    private String idea_name;
    private String idea_description;
    private String ideaFilepath;
    private String filename;
    private String lkp_email;

    public CreateIdeasRequest(int lkp_idea_cat1, int lkp_idea_cat2, int lkp_idea_cat3, String idea_name, String idea_description, String ideaFilepath, String filename, String lkp_email) {
        this.lkp_idea_cat1 = lkp_idea_cat1;
        this.lkp_idea_cat2 = lkp_idea_cat2;
        this.lkp_idea_cat3 = lkp_idea_cat3;
        this.idea_name = idea_name;
        this.idea_description = idea_description;
        this.ideaFilepath = ideaFilepath;
        this.filename = filename;
        this.lkp_email = lkp_email;
    }
}