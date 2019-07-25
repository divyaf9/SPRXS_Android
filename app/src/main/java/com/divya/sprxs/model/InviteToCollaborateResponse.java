package com.divya.sprxs.model;

public class InviteToCollaborateResponse {
    private int Id;
    private String error;

    public InviteToCollaborateResponse(int id, String error) {
        Id = id;
        this.error = error;
    }

    public int getId() {
        return Id;
    }

    public String getError() {
        return error;
    }
}
