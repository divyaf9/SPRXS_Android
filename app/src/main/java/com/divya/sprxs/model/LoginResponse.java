package com.divya.sprxs.model;

public class LoginResponse {

    private String token;
    private String profile_type;
    private String login_response;
    private String login_message;
    private String login_email;
    private String refresh_token;
    private String login_surname;
    private String login_firstname;
    private String error;

    public LoginResponse(String token, String profile_type, String login_response, String login_message, String login_email, String refresh_token, String login_surname, String login_firstname, String error) {
        this.token = token;
        this.profile_type = profile_type;
        this.login_response = login_response;
        this.login_message = login_message;
        this.login_email = login_email;
        this.refresh_token = refresh_token;
        this.login_surname = login_surname;
        this.login_firstname = login_firstname;
        this.error = error;
    }

    public String getToken() {
        return token;
    }

    public String getProfile_type() {
        return profile_type;
    }

    public String getLogin_response() {
        return login_response;
    }

    public String getLogin_message() {
        return login_message;
    }

    public String getLogin_email() {
        return login_email;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getLogin_surname() {
        return login_surname;
    }

    public String getLogin_firstname() {
        return login_firstname;
    }

    public String getError() {
        return error;
    }
}