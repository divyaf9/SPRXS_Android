package com.divya.sprxs.model;

public class Ideas {

    private String ideaId;
    private String ideaName;
    private String ideaDescription;
    private int equity;
    private int pendingEquity;
    private String IdeaDateCreated;
    private Boolean Searchable;
    private String OwnerFirstname;
    private String OwnerLastname;
    private String OwnerFirebaseId;
    private String IdeaStatus;
    private String blockchainId;

    public Ideas(String ideaId, String ideaName, String ideaDescription, int equity, int pendingEquity, String ideaDateCreated, Boolean searchable, String ownerFirstname, String ownerLastname, String ownerFirebaseId, String ideaStatus, String blockchainId) {
        this.ideaId = ideaId;
        this.ideaName = ideaName;
        this.ideaDescription = ideaDescription;
        this.equity = equity;
        this.pendingEquity = pendingEquity;
        IdeaDateCreated = ideaDateCreated;
        Searchable = searchable;
        OwnerFirstname = ownerFirstname;
        OwnerLastname = ownerLastname;
        OwnerFirebaseId = ownerFirebaseId;
        IdeaStatus = ideaStatus;
        this.blockchainId = blockchainId;
    }

    public String getIdeaId() {
        return ideaId;
    }

    public String getIdeaName() {
        return ideaName;
    }

    public String getIdeaDescription() {
        return ideaDescription;
    }

    public int getEquity() {
        return equity;
    }

    public int getPendingEquity() {
        return pendingEquity;
    }

    public String getIdeaDateCreated() {
        return IdeaDateCreated;
    }

    public Boolean getSearchable() {
        return Searchable;
    }

    public String getOwnerFirstname() {
        return OwnerFirstname;
    }

    public String getOwnerLastname() {
        return OwnerLastname;
    }

    public String getOwnerFirebaseId() {
        return OwnerFirebaseId;
    }

    public String getIdeaStatus() {
        return IdeaStatus;
    }

    public String getBlockchainId() {
        return blockchainId;
    }
}
