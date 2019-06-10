package com.divya.sprxs.model;

public class EditIdeaResponse {

    private String idea_ID;
    private String new_tokenID;
    private String idea_name;
    private String mintedBy;
    private String editIdea_response;
    private String createIdea_response;
    private String error;

    public EditIdeaResponse(String idea_ID, String new_tokenID, String idea_name, String mintedBy, String editIdea_response, String createIdea_response, String error) {
        this.idea_ID = idea_ID;
        this.new_tokenID = new_tokenID;
        this.idea_name = idea_name;
        this.mintedBy = mintedBy;
        this.editIdea_response = editIdea_response;
        this.createIdea_response = createIdea_response;
        this.error = error;
    }

    public String getIdea_ID() {
        return idea_ID;
    }

    public String getNew_tokenID() {
        return new_tokenID;
    }

    public String getIdea_name() {
        return idea_name;
    }

    public String getMintedBy() {
        return mintedBy;
    }

    public String getEditIdea_response() {
        return editIdea_response;
    }

    public String getCreateIdea_response() {
        return createIdea_response;
    }

    public String getError() {
        return error;
    }
}
