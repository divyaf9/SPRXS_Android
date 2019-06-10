package com.divya.sprxs.model;

public class RefreshTokenResponse {

    private  String access_token;

    public RefreshTokenResponse(String access_token) {
        this.access_token = access_token;
    }

    public String getAccess_token() {
        return access_token;
    }
}
