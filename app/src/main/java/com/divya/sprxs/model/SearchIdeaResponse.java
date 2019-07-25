package com.divya.sprxs.model;

import android.icu.text.DateFormat;

public class SearchIdeaResponse {

            private String ideaName;
            private String ideaDescription;
            private String ideaFilepath;
            private  String ideaUniqueID;
            private String ideaDatCreated;
            private String androidDate;
            private String lkpIdeaCat1;
            private String lkpIdeaCat2;
            private String lkpIdeaCat3;
            private String tokenId;
            private Boolean allowCollaborate;
            private Boolean allowSearch;
            private Boolean allowSale;
            private Boolean allowPublic;
            private String collabSynopsis;
            private String collabSkillsRequired;
            private Boolean pendingApproval;
            private String error;

    public SearchIdeaResponse(String ideaName, String ideaDescription, String ideaFilepath, String ideaUniqueID, String ideaDatCreated, String androidDate, String lkpIdeaCat1, String lkpIdeaCat2, String lkpIdeaCat3, String tokenId, Boolean allowCollaborate, Boolean allowSearch, Boolean allowSale, Boolean allowPublic, String collabSynopsis, String collabSkillsRequired, Boolean pendingApproval, String error) {
        this.ideaName = ideaName;
        this.ideaDescription = ideaDescription;
        this.ideaFilepath = ideaFilepath;
        this.ideaUniqueID = ideaUniqueID;
        this.ideaDatCreated = ideaDatCreated;
        this.androidDate = androidDate;
        this.lkpIdeaCat1 = lkpIdeaCat1;
        this.lkpIdeaCat2 = lkpIdeaCat2;
        this.lkpIdeaCat3 = lkpIdeaCat3;
        this.tokenId = tokenId;
        this.allowCollaborate = allowCollaborate;
        this.allowSearch = allowSearch;
        this.allowSale = allowSale;
        this.allowPublic = allowPublic;
        this.collabSynopsis = collabSynopsis;
        this.collabSkillsRequired = collabSkillsRequired;
        this.pendingApproval = pendingApproval;
        this.error = error;
    }

    public String getIdeaName() {
        return ideaName;
    }

    public String getIdeaDescription() {
        return ideaDescription;
    }

    public String getIdeaFilepath() {
        return ideaFilepath;
    }

    public String getIdeaUniqueID() {
        return ideaUniqueID;
    }

    public String getIdeaDatCreated() {
        return ideaDatCreated;
    }

    public String getAndroidDate() {
        return androidDate;
    }

    public String getLkpIdeaCat1() {
        return lkpIdeaCat1;
    }

    public String getLkpIdeaCat2() {
        return lkpIdeaCat2;
    }

    public String getLkpIdeaCat3() {
        return lkpIdeaCat3;
    }

    public String getTokenId() {
        return tokenId;
    }

    public Boolean getAllowCollaborate() {
        return allowCollaborate;
    }

    public Boolean getAllowSearch() {
        return allowSearch;
    }

    public Boolean getAllowSale() {
        return allowSale;
    }

    public Boolean getAllowPublic() {
        return allowPublic;
    }

    public String getCollabSynopsis() {
        return collabSynopsis;
    }

    public String getCollabSkillsRequired() {
        return collabSkillsRequired;
    }

    public Boolean getPendingApproval() {
        return pendingApproval;
    }

    public String getError() {
        return error;
    }
}
