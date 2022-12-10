package com.example.coursereschedule.Model;

public class subjectListModel {
    String subjectCode;
    String subjectName;

    public subjectListModel(String subjectCode, String subjectName) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
    }

    public subjectListModel(){}

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode){
        this.subjectCode= subjectCode;
    }

    public String getSubjectName(){
        return subjectName;
    }

    public void setSubjectName(String subjectName){
        this.subjectName = subjectName;
    }
}
