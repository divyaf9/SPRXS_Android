package com.divya.sprxs.model;

public class ApproveRejectCollaboratorRequestsRequest {
    private Long profile_id;
    private String idea_id;
    private String decision;

    public ApproveRejectCollaboratorRequestsRequest(Long profile_id, String idea_id, String decision) {
        this.profile_id = profile_id;
        this.idea_id = idea_id;
        this.decision = decision;
    }
}
