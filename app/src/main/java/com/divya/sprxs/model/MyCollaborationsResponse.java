package com.divya.sprxs.model;

import java.util.ArrayList;
import java.util.List;

public class MyCollaborationsResponse {
    private Long id;
    private int totalEquity;
    private int totalPendingEquity;
    private List<Ideas> ideas = new ArrayList<>();
    private String error;

    public MyCollaborationsResponse(Long id, int totalEquity, int totalPendingEquity, List<Ideas> ideas, String error) {
        this.id = id;
        this.totalEquity = totalEquity;
        this.totalPendingEquity = totalPendingEquity;
        this.ideas = ideas;
        this.error = error;
    }

    public Long getId() {
        return id;
    }

    public int getTotalEquity() {
        return totalEquity;
    }

    public int getTotalPendingEquity() {
        return totalPendingEquity;
    }

    public List<Ideas> getIdeas() {
        return ideas;
    }

    public String getError() {
        return error;
    }
}
