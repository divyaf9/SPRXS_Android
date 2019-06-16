package com.divya.sprxs.model;

public class MyIdeasSummaryResponse {

    private String idea_name;
    private String ideaDescription;
    private String tokenId;
    private String idea_id;
    private String no_of_collaborators;
    private boolean allowSearch;
    private int lkpIdeaCat1;
    private String date_created;
    private boolean idea_status;
    private String tokens_owned;
    private Long date_created_timestamp;
    private String getDashboard_response;
    private String getDashboard_message;
    private String error;

    public MyIdeasSummaryResponse(String idea_name, String ideaDescription, String tokenId, String idea_id, String no_of_collaborators, boolean allowSearch, int lkpIdeaCat1, String date_created, boolean idea_status, String tokens_owned, Long date_created_timestamp, String getDashboard_response, String getDashboard_message, String error) {
        this.idea_name = idea_name;
        this.ideaDescription = ideaDescription;
        this.tokenId = tokenId;
        this.idea_id = idea_id;
        this.no_of_collaborators = no_of_collaborators;
        this.allowSearch = allowSearch;
        this.lkpIdeaCat1 = lkpIdeaCat1;
        this.date_created = date_created;
        this.idea_status = idea_status;
        this.tokens_owned = tokens_owned;
        this.date_created_timestamp = date_created_timestamp;
        this.getDashboard_response = getDashboard_response;
        this.getDashboard_message = getDashboard_message;
        this.error = error;
    }

    public String getIdea_name() {
        return idea_name;
    }

    public String getIdeaDescription() {
        return ideaDescription;
    }

    public String getTokenId() {
        return tokenId;
    }

    public String getIdea_id() {
        return idea_id;
    }

    public String getNo_of_collaborators() {
        return no_of_collaborators;
    }

    public boolean isAllowSearch() {
        return allowSearch;
    }

    public int getLkpIdeaCat1() {
        return lkpIdeaCat1;
    }

    public String getDate_created() {
        return date_created;
    }

    public boolean isIdea_status() {
        return idea_status;
    }

    public String getTokens_owned() {
        return tokens_owned;
    }

    public Long getDate_created_timestamp() {
        return date_created_timestamp;
    }

    public String getGetDashboard_response() {
        return getDashboard_response;
    }

    public String getGetDashboard_message() {
        return getDashboard_message;
    }

    public String getError() {
        return error;
    }
}
