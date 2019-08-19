package com.divya.sprxs.model;

public class LodgeConsensusResponse {

    private String LC_response;
    private String error;

    public LodgeConsensusResponse(String LC_response, String error) {
        this.LC_response = LC_response;
        this.error = error;
    }

    public String getLC_response() {
        return LC_response;
    }

    public String getError() {
        return error;
    }
}
