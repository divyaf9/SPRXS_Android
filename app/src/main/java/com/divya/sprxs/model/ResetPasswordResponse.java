package com.divya.sprxs.model;

public class ResetPasswordResponse   {

    private String resetPW_message;
    private String resetPW_response;

    public ResetPasswordResponse(String resetPW_message, String resetPW_response) {
        this.resetPW_message = resetPW_message;
        this.resetPW_response = resetPW_response;
    }

    public String getResetPW_message() {
        return resetPW_message;
    }

    public String getResetPW_response() {
        return resetPW_response;
    }
}