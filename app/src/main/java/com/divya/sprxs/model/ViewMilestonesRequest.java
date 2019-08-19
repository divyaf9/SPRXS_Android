package com.divya.sprxs.model;

public class ViewMilestonesRequest {

    private int id;
    private String idea_id;
    private Long collab_id;
    private String owner_profile;

    public ViewMilestonesRequest(int id, String idea_id, Long collab_id, String owner_profile) {
        this.id = id;
        this.idea_id = idea_id;
        this.collab_id = collab_id;
        this.owner_profile = owner_profile;
    }

    public int getId() {
        return id;
    }

    public String getIdea_id() {
        return idea_id;
    }

    public Long getCollab_id() {
        return collab_id;
    }

    public String getOwner_profile() {
        return owner_profile;
    }
}
