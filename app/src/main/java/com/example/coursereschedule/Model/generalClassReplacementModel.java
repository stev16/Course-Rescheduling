package com.example.coursereschedule.Model;

public class generalClassReplacementModel {

    String replacementSubjectCode;
    String replacementSubjectDay;
    String replacementSubjectName;
    String replacementSubjectTime;
    String replacementSubjectDate;
    String replacementSubjectVenue;



    public generalClassReplacementModel(String replacementSubjectCode,  String replacementSubjectName, String replacementSubjectDate, String replacementSubjectDay, String replacementSubjectTime, String replacementSubjectVenue) {
        this.replacementSubjectCode = replacementSubjectCode;
        this.replacementSubjectName = replacementSubjectName;
        this.replacementSubjectDate = replacementSubjectDate;
        this.replacementSubjectDay =replacementSubjectDay;
        this.replacementSubjectTime =replacementSubjectTime;
        this.replacementSubjectVenue =replacementSubjectVenue;


    }

    public generalClassReplacementModel(){}


    public String getReplacementSubjectCode() {
        return replacementSubjectCode;
    }

    public void setReplacementSubjectCode(String replacementSubjectCode) {
        this.replacementSubjectCode= replacementSubjectCode;
    }

    public String getReplacementSubjectName() {
        return replacementSubjectName;
    }

    public void setReplacementSubjectName(String replacementSubjectName) {
        this.replacementSubjectName = replacementSubjectName;
    }

    public String getReplacementSubjectDate(){
        return replacementSubjectDate;
    }

    public void setReplacementSubjectDate(String replacementSubjectDate){
        this.replacementSubjectDate = replacementSubjectDate;
    }

    public String getReplacementSubjectDay() {
        return replacementSubjectDay;
    }

    public void setReplacementSubjectDay(String replacementSubjectDay) {
        this.replacementSubjectDay = replacementSubjectDay;
    }

    public String getReplacementSubjectTime() {
        return replacementSubjectTime;
    }

    public void setReplacementSubjectTime(String replacementSubjectTime) {
        this.replacementSubjectTime = replacementSubjectTime;
    }

    public String getReplacementSubjectVenue() {
        return replacementSubjectVenue;
    }

    public void setReplacementSubjectVenue(String replacementSubjectVenue) {
        this.replacementSubjectVenue = replacementSubjectVenue;
    }
}
