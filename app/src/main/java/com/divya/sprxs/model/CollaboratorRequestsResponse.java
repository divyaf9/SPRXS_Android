package com.divya.sprxs.model;

public class CollaboratorRequestsResponse {

    private int id;
    private String ideaId;
    private String ownerProfile;
    private int lkpWoiRole;
    private int lkpWoiCapacity;
    private int lkpWoiRemun;
    private String collabReason;
    private String collabValueadd;
    private String collabFilepath;
    private String bxnTxnId;
    private Boolean emailSent;
    private int collabApproved;
    private Long dateCreated;
    private String firstname;
    private String lastname;
    private Long profileId;
    private String collaboratorFirebaseUid;
    private String error;

    public CollaboratorRequestsResponse(int id, String ideaId, String ownerProfile, int lkpWoiRole, int lkpWoiCapacity, int lkpWoiRemun, String collabReason, String collabValueadd, String collabFilepath, String bxnTxnId, Boolean emailSent, int collabApproved, Long dateCreated, String firstname, String lastname, Long profileId, String collaboratorFirebaseUid, String error) {
        this.id = id;
        this.ideaId = ideaId;
        this.ownerProfile = ownerProfile;
        this.lkpWoiRole = lkpWoiRole;
        this.lkpWoiCapacity = lkpWoiCapacity;
        this.lkpWoiRemun = lkpWoiRemun;
        this.collabReason = collabReason;
        this.collabValueadd = collabValueadd;
        this.collabFilepath = collabFilepath;
        this.bxnTxnId = bxnTxnId;
        this.emailSent = emailSent;
        this.collabApproved = collabApproved;
        this.dateCreated = dateCreated;
        this.firstname = firstname;
        this.lastname = lastname;
        this.profileId = profileId;
        this.collaboratorFirebaseUid = collaboratorFirebaseUid;
        this.error = error;
    }

    public int getId() {
        return id;
    }

    public String getIdeaId() {
        return ideaId;
    }

    public String getOwnerProfile() {
        return ownerProfile;
    }

    public int getLkpWoiRole() {
        return lkpWoiRole;
    }

    public int getLkpWoiCapacity() {
        return lkpWoiCapacity;
    }

    public int getLkpWoiRemun() {
        return lkpWoiRemun;
    }

    public String getCollabReason() {
        return collabReason;
    }

    public String getCollabValueadd() {
        return collabValueadd;
    }

    public String getCollabFilepath() {
        return collabFilepath;
    }

    public String getBxnTxnId() {
        return bxnTxnId;
    }

    public Boolean getEmailSent() {
        return emailSent;
    }

    public int getCollabApproved() {
        return collabApproved;
    }

    public Long getDateCreated() {
        return dateCreated;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Long getProfileId() {
        return profileId;
    }

    public String getCollaboratorFirebaseUid() {
        return collaboratorFirebaseUid;
    }

    public String getError() {
        return error;
    }
}
