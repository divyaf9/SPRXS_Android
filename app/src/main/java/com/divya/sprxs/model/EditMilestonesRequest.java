package com.divya.sprxs.model;

public class EditMilestonesRequest {

    private int id;
    private int approval;
    private String idea_id;
    private String milestone_name;
    private String milestone_description;
    private String agreed_completion_date;

    public EditMilestonesRequest(int id, int approval, String idea_id, String milestone_name, String milestone_description, String agreed_completion_date) {
        this.id = id;
        this.approval = approval;
        this.idea_id = idea_id;
        this.milestone_name = milestone_name;
        this.milestone_description = milestone_description;
        this.agreed_completion_date = agreed_completion_date;
    }
}
