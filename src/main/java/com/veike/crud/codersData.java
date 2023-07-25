package com.veike.crud;

public class codersData {
    private Integer coders_id;
    private String fullname;
    private String year;
    private String course;
    private String gender;

    public codersData(Integer coders_id, String fullname, String year, String course, String gender) {
        this.coders_id = coders_id;
        this.fullname = fullname;
        this.year = year;
        this.course = course;
        this.gender = gender;
    }

    public Integer getCoders_id() {
        return coders_id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getYear() {
        return year;
    }

    public String getCourse() {
        return course;
    }

    public String getGender() {
        return gender;
    }
}
