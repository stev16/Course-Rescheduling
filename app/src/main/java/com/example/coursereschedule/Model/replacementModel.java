package com.example.coursereschedule.Model;

import java.io.Serializable;

public class replacementModel implements Serializable {
    String subjectCode;
    String subjectName;
    String subjectTime;
    String subjectClass;
    String subjectDay;
    String subjectDate;

    public replacementModel(String subjectCode, String subjectName, String subjectTime, String subjectClass, String subjectDate, String subjectDay) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.subjectTime = subjectTime;
        this.subjectClass = subjectClass;
        this.subjectDay =subjectDay;
        this.subjectDate = subjectDate;
    }

    public replacementModel(){}


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

    public String getSubjectTime() {
        return subjectTime;
    }

    public void setSubjectTime(String subjectTime) {
        this.subjectTime = subjectTime;
    }

    public String getSubjectClass() {
        return subjectClass;
    }

    public void setSubjectClass(String subjectClass) {
        this.subjectClass = subjectClass;
    }

    public String getSubjectDay() {
        return subjectDay;
    }

    public void setSubjectDay(String subjectDay) {
        this.subjectDay = subjectDay;
    }

    public String getSubjectDate() {
        return subjectDate;
    }

    public void setSubjectDate(String subjectDate) {
        this.subjectDate = subjectDate;
    }
}
