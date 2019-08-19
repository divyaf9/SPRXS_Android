package com.divya.sprxs.model;

public class ApproveRejectCollaboratorRequestsResponse {
    private String ACOL_profile_id;
    private String ACOL_idea_id;
    private String ACOL_response;
    private String ACOL_getCollabApproved;
    private String error;

    public ApproveRejectCollaboratorRequestsResponse(String ACOL_profile_id, String ACOL_idea_id, String ACOL_response, String ACOL_getCollabApproved, String error) {
        this.ACOL_profile_id = ACOL_profile_id;
        this.ACOL_idea_id = ACOL_idea_id;
        this.ACOL_response = ACOL_response;
        this.ACOL_getCollabApproved = ACOL_getCollabApproved;
        this.error = error;
    }

    public String getACOL_profile_id() {
        return ACOL_profile_id;
    }

    public String getACOL_idea_id() {
        return ACOL_idea_id;
    }

    public String getACOL_response() {
        return ACOL_response;
    }

    public String getACOL_getCollabApproved() {
        return ACOL_getCollabApproved;
    }

    public String getError() {
        return error;
    }
}
