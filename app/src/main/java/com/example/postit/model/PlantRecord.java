package com.example.postit.model;

import com.google.type.DateTime;

public class PlantRecord {
    int humid;
    float temp;
    DateTime myTimestamp;

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

    public DateTime getMyTimestamp() {
        return myTimestamp;
    }

    public void setMyTimestamp(DateTime myTimestamp) {
        this.myTimestamp = myTimestamp;
    }
}
