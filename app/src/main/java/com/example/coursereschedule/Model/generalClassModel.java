package com.example.coursereschedule.Model;

public class generalClassModel {
    String subjectName;
    String subjectCode;
    String subjectDay;
    String subjectTime;
    String subjectVenue;
    String subjectType;
    String lecturer;

    public generalClassModel(String subjectCode, String subjectName, String subjectDay, String subjectTime, String subjectVenue, String subjectType, String lecturer) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.subjectDay =subjectDay;
        this.subjectTime =subjectTime;
        this.subjectVenue =subjectVenue;
        this.subjectType =subjectType;
        this.lecturer = lecturer;
    }

    public generalClassModel(){}


    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectDay() {
        return subjectDay;
    }

    public void setSubjectDay(String subjectDay) {
        this.subjectDay = subjectDay;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer= lecturer;
    }

    public String getSubjectTime() {return subjectTime; }

    public void setSubjectTime(String subjectTime) {this.subjectTime = subjectTime; }

    public String getSubjectVenue() {return subjectVenue; }

    public void setSubjectVenue(String subjectVenue) {this.subjectVenue = subjectVenue; }

    public String getSubjectType() {return subjectType; }

    public void setSubjectType(String subjectType) {this.subjectType = subjectType; }
}
