package com.divya.sprxs.model;

public class ViewMilestonesResponse {

    private int id;
    private String ownerProfile;
    private String milestoneDateCreated;
    private String ideaId;
    private String milestoneName;
    private String firstname;
    private String surname;
    private String milestoneDescription;
    private String agreedCompletionDate;
    private String agreedCompletionTimestamp;
    private int offeredTokens;
    private int approval;

    public ViewMilestonesResponse(int id, String ownerProfile, String milestoneDateCreated, String ideaId, String milestoneName, String firstname, String surname, String milestoneDescription, String agreedCompletionDate, String agreedCompletionTimestamp, int offeredTokens, int approval) {
        this.id = id;
        this.ownerProfile = ownerProfile;
        this.milestoneDateCreated = milestoneDateCreated;
        this.ideaId = ideaId;
        this.milestoneName = milestoneName;
        this.firstname = firstname;
        this.surname = surname;
        this.milestoneDescription = milestoneDescription;
        this.agreedCompletionDate = agreedCompletionDate;
        this.agreedCompletionTimestamp = agreedCompletionTimestamp;
        this.offeredTokens = offeredTokens;
        this.approval = approval;
    }

    public int getId() {
        return id;
    }

    public String getOwnerProfile() {
        return ownerProfile;
    }

    public String getMilestoneDateCreated() {
        return milestoneDateCreated;
    }

    public String getIdeaId() {
        return ideaId;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public String getMilestoneDescription() {
        return milestoneDescription;
    }

    public String getAgreedCompletionDate() {
        return agreedCompletionDate;
    }

    public String getAgreedCompletionTimestamp() {
        return agreedCompletionTimestamp;
    }

    public int getOfferedTokens() {
        return offeredTokens;
    }

    public int getApproval() {
        return approval;
    }
}
