package com.divya.sprxs.model;

public class RequestWorkOnIdeaResponse {

    private String tokenID;
    private String mintedBy;
    private String ideaID;
    private String error;

    public RequestWorkOnIdeaResponse(String tokenID, String mintedBy, String ideaID, String error) {
        this.tokenID = tokenID;
        this.mintedBy = mintedBy;
        this.ideaID = ideaID;
        this.error = error;
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

    public String getError() {
        return error;
    }
}
