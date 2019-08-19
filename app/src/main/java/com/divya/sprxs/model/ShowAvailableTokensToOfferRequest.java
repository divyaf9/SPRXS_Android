package com.divya.sprxs.model;

public class ShowAvailableTokensToOfferRequest {

    private String idea_uniqueid;
    private String  profile;

    public ShowAvailableTokensToOfferRequest(String idea_uniqueid, String profile) {
        this.idea_uniqueid = idea_uniqueid;
        this.profile = profile;
    }


}
