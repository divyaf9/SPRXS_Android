package com.divya.sprxs.model;

public class Education{

    private Long id;
    private String schoolName;
    private String qualification;
    private String major;
    private String country;
    private int year;

    public Education(Long id, String schoolName, String qualification, String major, String country, int year) {
        this.id = id;
        this.schoolName = schoolName;
        this.qualification = qualification;
        this.major = major;
        this.country = country;
        this.year = year;
    }

    public Long getId() {
        return id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getQualification() {
        return qualification;
    }

    public String getMajor() {
        return major;
    }

    public String getCountry() {
        return country;
    }

    public int getYear() {
        return year;
    }
}
