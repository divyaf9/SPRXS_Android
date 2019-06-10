package com.divya.sprxs.model;

public class LoginRequest {

    private String email_add;
    private String passwrd;

    public LoginRequest(String email_add, String passwrd) {
        this.email_add = email_add;
        this.passwrd = passwrd;
    }
}