package com.divya.sprxs.model;

public class LodgeConsensusRequest {

    private int milestone_id;
    private String profile;
    private int approval;

    public LodgeConsensusRequest(int milestone_id, String profile, int approval) {
        this.milestone_id = milestone_id;
        this.profile = profile;
        this.approval = approval;
    }
}
