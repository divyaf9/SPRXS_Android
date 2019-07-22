package com.divya.sprxs.model;

public class InviteToCollaborateRequest {
    private Long collaboratorID;
    private String ideaID;


    public InviteToCollaborateRequest(Long collaboratorID, String ideaID) {
        this.collaboratorID = collaboratorID;
        this.ideaID = ideaID;
    }
}
