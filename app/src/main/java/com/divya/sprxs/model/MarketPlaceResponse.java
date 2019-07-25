package com.divya.sprxs.model;

import java.util.ArrayList;
import java.util.List;

public class MarketPlaceResponse {
private Long id;
private String firstname;
private String surname;
private Boolean optInComp;
private Boolean optInNotif;
private int profileType;
private String firebaseUid;
private Boolean desktopNotif;
private String profileDesc;
private int primaryExpertise;
private int secondaryExpertise;
private String primaryOther;
private String secondaryOther;
private Boolean hidden;
private int orgId;
private int lkpCountry;
private int mobileCc;
private String mobileNo;
private String error;

public List<Education> educations = new ArrayList<Education>();
public List<Weblink> weblinks = new ArrayList<Weblink>();
public  List<Skills>  skills = new ArrayList<Skills>();

    public MarketPlaceResponse(Long id, String firstname, String surname, Boolean optInComp, Boolean optInNotif, int profileType, String firebaseUid, Boolean desktopNotif, String profileDesc, int primaryExpertise, int secondaryExpertise, String primaryOther, String secondaryOther, Boolean hidden, int orgId, int lkpCountry, int mobileCc, String mobileNo, String error, List<Education> educations, List<Weblink> weblinks, List<Skills> skills) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.optInComp = optInComp;
        this.optInNotif = optInNotif;
        this.profileType = profileType;
        this.firebaseUid = firebaseUid;
        this.desktopNotif = desktopNotif;
        this.profileDesc = profileDesc;
        this.primaryExpertise = primaryExpertise;
        this.secondaryExpertise = secondaryExpertise;
        this.primaryOther = primaryOther;
        this.secondaryOther = secondaryOther;
        this.hidden = hidden;
        this.orgId = orgId;
        this.lkpCountry = lkpCountry;
        this.mobileCc = mobileCc;
        this.mobileNo = mobileNo;
        this.error = error;
        this.educations = educations;
        this.weblinks = weblinks;
        this.skills = skills;
    }

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public Boolean getOptInComp() {
        return optInComp;
    }

    public Boolean getOptInNotif() {
        return optInNotif;
    }

    public int getProfileType() {
        return profileType;
    }

    public String getFirebaseUid() {
        return firebaseUid;
    }

    public Boolean getDesktopNotif() {
        return desktopNotif;
    }

    public String getProfileDesc() {
        return profileDesc;
    }

    public int getPrimaryExpertise() {
        return primaryExpertise;
    }

    public int getSecondaryExpertise() {
        return secondaryExpertise;
    }

    public String getPrimaryOther() {
        return primaryOther;
    }

    public String getSecondaryOther() {
        return secondaryOther;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public int getOrgId() {
        return orgId;
    }

    public int getLkpCountry() {
        return lkpCountry;
    }

    public int getMobileCc() {
        return mobileCc;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getError() {
        return error;
    }

    public List<Education> getEducations() {
        return educations;
    }

    public List<Weblink> getWeblinks() {
        return weblinks;
    }

    public List<Skills> getSkills() {
        return skills;
    }
}