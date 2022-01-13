package com.example.postit.model;

import com.google.type.DateTime;

import java.util.Date;

public class PlantRecord {
    int humid;
    float temp;
    Date myTimestamp;

    public int getHumid() {
        return humid;
    }

    public void setHumid(int humid) {
        this.humid = humid;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public Date getMyTimestamp() {
        return myTimestamp;
    }

    public void setMyTimestamp(Date myTimestamp) {
        this.myTimestamp = myTimestamp;
    }
}
