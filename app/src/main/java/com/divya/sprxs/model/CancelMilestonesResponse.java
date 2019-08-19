package com.divya.sprxs.model;

public class CancelMilestonesResponse {

    private String response;
    private String error;

    public CancelMilestonesResponse(String response, String error) {
        this.response = response;
        this.error = error;
    }

    public String getResponse() {
        return response;
    }

    public String getError() {
        return error;
    }
}
