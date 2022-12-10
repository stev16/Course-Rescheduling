package com.example.coursereschedule.Model;

import java.io.Serializable;

public class timeModel implements Serializable {
    String TimeOn;

    public timeModel(String TimeOn) {
        this.TimeOn = TimeOn;
    }

    public timeModel(){}


    public String getTimeOn() {
        return TimeOn;
    }

    public void setTimeOn(String TimeOn) {
        this.TimeOn = TimeOn;
    }


}
