package com.divya.sprxs.model;

public class CreateProfileResponse {

    private String createProfile_response;
    private String createProfile_message;

    public CreateProfileResponse(String createProfile_response, String createProfile_message) {
        this.createProfile_response = createProfile_response;
        this.createProfile_message = createProfile_message;
    }

    public String getCreateProfile_response() {
        return createProfile_response;
    }

    public String getCreateProfile_message() {
        return createProfile_message;
    }
}
