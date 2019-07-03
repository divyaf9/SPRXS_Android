package com.divya.sprxs.model;

public class RequestWorkOnIdeaResponse {

    private String tokenID;
    private String mintedBy;
    private String ideaID;

    public RequestWorkOnIdeaResponse(String tokenID, String mintedBy, String ideaID) {
        this.tokenID = tokenID;
        this.mintedBy = mintedBy;
        this.ideaID = ideaID;
    }

    public String getTokenID() {
        return tokenID;
    }

    public String getMintedBy() {
        return mintedBy;
    }

    public String getIdeaID() {
        return ideaID;
    }
}
