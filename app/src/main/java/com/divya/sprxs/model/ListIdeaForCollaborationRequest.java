package com.divya.sprxs.model;

public class ListIdeaForCollaborationRequest {

    private String ideaUniqueSignature;
    private String collabSynopsis;
    private String collabSkillsRequired;

    public ListIdeaForCollaborationRequest(String ideaUniqueSignature, String collabSynopsis, String collabSkillsRequired) {
        this.ideaUniqueSignature = ideaUniqueSignature;
        this.collabSynopsis = collabSynopsis;
        this.collabSkillsRequired = collabSkillsRequired;
    }
}
