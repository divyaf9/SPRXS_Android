package com.divya.sprxs.model;

public class SearchIdeaRequest {

    private Long profile_id;
    private String idea_uniqueid;
    private String idea_name;
    private String idea_description;

    public SearchIdeaRequest(Long profile_id, String idea_uniqueid, String idea_name, String idea_description) {
        this.profile_id = profile_id;
        this.idea_uniqueid = idea_uniqueid;
        this.idea_name = idea_name;
        this.idea_description = idea_description;
    }

}
