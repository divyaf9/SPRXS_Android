package com.divya.sprxs.model;

public class RequestWorkOnIdeaRequest {

    private Long profile_id;
    private String idea_id;
    private int lkp_woi_role;
    private int lkp_woi_capacity;
    private int lkp_woi_remun;
    private String collab_reason;
    private String collab_valueadd;

    public RequestWorkOnIdeaRequest(Long profile_id, String idea_id, int lkp_woi_role, int lkp_woi_capacity, int lkp_woi_remun, String collab_reason, String collab_valueadd) {
        this.profile_id = profile_id;
        this.idea_id = idea_id;
        this.lkp_woi_role = lkp_woi_role;
        this.lkp_woi_capacity = lkp_woi_capacity;
        this.lkp_woi_remun = lkp_woi_remun;
        this.collab_reason = collab_reason;
        this.collab_valueadd = collab_valueadd;
    }
}
