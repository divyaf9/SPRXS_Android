package com.divya.sprxs.model;

public class ContactUsResponse {

    private String error;

    public ContactUsResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
