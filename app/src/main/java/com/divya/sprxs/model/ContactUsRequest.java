package com.divya.sprxs.model;

public class ContactUsRequest {

    private String reason;
    private String modeOfContact;
    private Long reportersId;

    public ContactUsRequest(String reason, String modeOfContact, Long reportersId) {
        this.reason = reason;
        this.modeOfContact = modeOfContact;
        this.reportersId = reportersId;
    }
}
