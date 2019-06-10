package com.divya.sprxs.model;

public class MyIdeasSummaryRequest {

    String lkp_email;

    public MyIdeasSummaryRequest(String lkp_email) {
        this.lkp_email = lkp_email;
    }

    public String getLkp_email() {
        return lkp_email;
    }
}
