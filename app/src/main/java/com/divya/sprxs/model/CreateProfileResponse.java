package com.divya.sprxs.model;

public class CreateProfileResponse {

    private String createProfile_response;
    private String createProfile_message;
    private String refresh_token;
    private String token;
    private String profile_id;
    private String error;

    public CreateProfileResponse(String createProfile_response, String createProfile_message, String refresh_token, String token, String profile_id, String error) {
        this.createProfile_response = createProfile_response;
        this.createProfile_message = createProfile_message;
        this.refresh_token = refresh_token;
        this.token = token;
        this.profile_id = profile_id;
        this.error = error;
    }

    public String getCreateProfile_response() {
        return createProfile_response;
    }

    public String getCreateProfile_message() {
        return createProfile_message;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getToken() {
        return token;
    }

    public String getProfile_id() {
        return profile_id;
    }

    public String getError() {
        return error;
    }
}
