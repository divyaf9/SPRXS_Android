package com.divya.sprxs.model;

public class ShowEquityForIdeaResponse {
    private int id;
    private String firstname;
    private String lastname;
    private Long    profileId;
    private String ideaUniqueId;
    private String dateCreated;
    private int tokensOwned;
    private String firebaseUid;

    public ShowEquityForIdeaResponse(int id, String firstname, String lastname, Long profileId, String ideaUniqueId, String dateCreated, int tokensOwned, String firebaseUid) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.profileId = profileId;
        this.ideaUniqueId = ideaUniqueId;
        this.dateCreated = dateCreated;
        this.tokensOwned = tokensOwned;
        this.firebaseUid = firebaseUid;
    }

    public int getId() {
        return id;
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

    public String getIdeaUniqueId() {
        return ideaUniqueId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public int getTokensOwned() {
        return tokensOwned;
    }

    public String getFirebaseUid() {
        return firebaseUid;
    }
}
