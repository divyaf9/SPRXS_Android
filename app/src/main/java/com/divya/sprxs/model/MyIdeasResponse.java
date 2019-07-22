package com.divya.sprxs.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MyIdeasResponse {

    private String ideaName;
    private String ideaDescription;
    private String ideaFilepath;
    private String ideaUniqueID;
    private String ideaDateCreated;
    private String androidDate;
    private int lkpIdeaCat1;
    private int lkpIdeaCat2;
    private int lkpIdeaCat3;
    private int lkpIdeaStatus;
    private String tokenId;
    private boolean allowCollaborate;
    private boolean allowSearch;
    private boolean allowSale;
    private boolean allowPublic;
    private boolean collabSynopsis;
    private boolean collabSkillsRequired;
    private String error;

    public MyIdeasResponse(String ideaName, String ideaDescription, String ideaFilepath, String ideaUniqueID, String ideaDateCreated, String androidDate, int lkpIdeaCat1, int lkpIdeaCat2, int lkpIdeaCat3, int lkpIdeaStatus, String tokenId, boolean allowCollaborate, boolean allowSearch, boolean allowSale, boolean allowPublic, boolean collabSynopsis, boolean collabSkillsRequired, String error) {
        this.ideaName = ideaName;
        this.ideaDescription = ideaDescription;
        this.ideaFilepath = ideaFilepath;
        this.ideaUniqueID = ideaUniqueID;
        this.ideaDateCreated = ideaDateCreated;
        this.androidDate = androidDate;
        this.lkpIdeaCat1 = lkpIdeaCat1;
        this.lkpIdeaCat2 = lkpIdeaCat2;
        this.lkpIdeaCat3 = lkpIdeaCat3;
        this.lkpIdeaStatus = lkpIdeaStatus;
        this.tokenId = tokenId;
        this.allowCollaborate = allowCollaborate;
        this.allowSearch = allowSearch;
        this.allowSale = allowSale;
        this.allowPublic = allowPublic;
        this.collabSynopsis = collabSynopsis;
        this.collabSkillsRequired = collabSkillsRequired;
        this.error = error;
    }

    protected MyIdeasResponse(Parcel in) {
        ideaName = in.readString();
        ideaDescription = in.readString();
        ideaFilepath = in.readString();
        ideaUniqueID = in.readString();
        ideaDateCreated = in.readString();
        androidDate = in.readString();
        lkpIdeaCat1 = in.readInt();
        lkpIdeaCat2 = in.readInt();
        lkpIdeaCat3 = in.readInt();
        lkpIdeaStatus = in.readInt();
        tokenId = in.readString();
        allowCollaborate = in.readByte() != 0;
        allowSearch = in.readByte() != 0;
        allowSale = in.readByte() != 0;
        allowPublic = in.readByte() != 0;
        collabSynopsis = in.readByte() != 0;
        collabSkillsRequired = in.readByte() != 0;
        error = in.readString();
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

    public String getIdeaDateCreated() {
        return ideaDateCreated;
    }

    public String getAndroidDate() {
        return androidDate;
    }

    public int getLkpIdeaCat1() {
        return lkpIdeaCat1;
    }

    public int getLkpIdeaCat2() {
        return lkpIdeaCat2;
    }

    public int getLkpIdeaCat3() {
        return lkpIdeaCat3;
    }

    public int getLkpIdeaStatus() {
        return lkpIdeaStatus;
    }

    public String getTokenId() {
        return tokenId;
    }

    public boolean isAllowCollaborate() {
        return allowCollaborate;
    }

    public boolean isAllowSearch() {
        return allowSearch;
    }

    public boolean isAllowSale() {
        return allowSale;
    }

    public boolean isAllowPublic() {
        return allowPublic;
    }

    public boolean isCollabSynopsis() {
        return collabSynopsis;
    }

    public boolean isCollabSkillsRequired() {
        return collabSkillsRequired;
    }

    public String getError() {
        return error;
    }


}
