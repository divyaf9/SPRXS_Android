package com.divya.sprxs.model;

import java.sql.Struct;
import java.util.Date;

public class CreateMilestonesResponse {

    private String milestone_date_created;
    private String owner_profile;
    private String milestone_agreed_completion_date;
    private String milestone_description;
    private String approval;
    private String milestone_name;
    private String ID;
    private String idea_id;
    private String error;

    public CreateMilestonesResponse(String milestone_date_created, String owner_profile, String milestone_agreed_completion_date, String milestone_description, String approval, String milestone_name, String ID, String idea_id, String error) {
        this.milestone_date_created = milestone_date_created;
        this.owner_profile = owner_profile;
        this.milestone_agreed_completion_date = milestone_agreed_completion_date;
        this.milestone_description = milestone_description;
        this.approval = approval;
        this.milestone_name = milestone_name;
        this.ID = ID;
        this.idea_id = idea_id;
        this.error = error;
    }

    public String getMilestone_date_created() {
        return milestone_date_created;
    }

    public String getOwner_profile() {
        return owner_profile;
    }

    public String getMilestone_agreed_completion_date() {
        return milestone_agreed_completion_date;
    }

    public String getMilestone_description() {
        return milestone_description;
    }

    public String getApproval() {
        return approval;
    }

    public String getMilestone_name() {
        return milestone_name;
    }

    public String getID() {
        return ID;
    }

    public String getIdea_id() {
        return idea_id;
    }

    public String getError() {
        return error;
    }
}
