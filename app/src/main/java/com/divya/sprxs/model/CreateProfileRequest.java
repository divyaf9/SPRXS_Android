package com.divya.sprxs.model;

public class CreateProfileRequest {

    private int profileType;
    private String firstname;
    private String surname;
    private int lkp_country;
    private int mobile_cc;
    private String mobile_no;
    private String email_add;
    private String passwrd;
    private String passwrd2;
    private Boolean opt_in_comp;
    private Boolean opt_in_notif;
    private String firebase_uid;

    public CreateProfileRequest(int profileType, String firstname, String surname, int lkp_country, int mobile_cc, String mobile_no, String email_add, String passwrd, String passwrd2, Boolean opt_in_comp, Boolean opt_in_notif, String firebase_uid) {
        this.profileType = profileType;
        this.firstname = firstname;
        this.surname = surname;
        this.lkp_country = lkp_country;
        this.mobile_cc = mobile_cc;
        this.mobile_no = mobile_no;
        this.email_add = email_add;
        this.passwrd = passwrd;
        this.passwrd2 = passwrd2;
        this.opt_in_comp = opt_in_comp;
        this.opt_in_notif = opt_in_notif;
        this.firebase_uid = firebase_uid;
    }

    public int getProfileType() {
        return profileType;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public int getLkp_country() {
        return lkp_country;
    }

    public int getMobile_cc() {
        return mobile_cc;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public String getEmail_add() {
        return email_add;
    }

    public String getPasswrd() {
        return passwrd;
    }

    public String getPasswrd2() {
        return passwrd2;
    }

    public Boolean getOpt_in_comp() {
        return opt_in_comp;
    }

    public Boolean getOpt_in_notif() {
        return opt_in_notif;
    }

    public String getFirebase_uid() {
        return firebase_uid;
    }
}

