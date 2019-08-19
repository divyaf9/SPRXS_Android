package com.divya.sprxs.model;

import java.util.Date;

public class CreateMilestonesRequest {

    private Long collab_id;
    private String idea_id;
    private String milestone_name;
    private String milestone_description;
    private String agreed_completion_date;
    private int agreed_tokens;
    private Boolean equity_approved;

    public CreateMilestonesRequest(Long collab_id, String idea_id, String milestone_name, String milestone_description, String agreed_completion_date, int agreed_tokens, Boolean equity_approved) {
        this.collab_id = collab_id;
        this.idea_id = idea_id;
        this.milestone_name = milestone_name;
        this.milestone_description = milestone_description;
        this.agreed_completion_date = agreed_completion_date;
        this.agreed_tokens = agreed_tokens;
        this.equity_approved = equity_approved;
    }


}
