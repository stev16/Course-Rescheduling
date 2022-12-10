package com.example.coursereschedule.Model;

public class userModel {
    String name, userID, email, password;

    public userModel(){

    }

    public userModel(String name, String userID, String email, String password) {
        this.name = name;
        this.userID = userID;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserID(){ return userID; }

    public void setUserID(String userID){this.userID = userID; }
}
