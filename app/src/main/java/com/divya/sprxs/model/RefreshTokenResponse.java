package com.divya.sprxs.model;

public class RefreshTokenResponse {

    private  String access_token;
    private String error;

    public RefreshTokenResponse(String access_token, String error) {
        this.access_token = access_token;
        this.error = error;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getError() {
        return error;
    }
}
