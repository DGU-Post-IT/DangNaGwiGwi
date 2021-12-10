package com.example.postit.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AudioRecord {
    private String speakerID;
    @ServerTimestamp
    private Date time;
    private String audioLocation;
    private int questionId;
    private int emotion;

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    AudioRecord(){}

    public AudioRecord(String speakerID, String audioLocation, int emotion, int questionId) {
        this.speakerID = speakerID;
        this.audioLocation = audioLocation;
        this.emotion = emotion;
        this.questionId = questionId;
    }

    public String getSpeakerID() {
        return speakerID;
    }

    public void setSpeakerID(String speakerID) {
        this.speakerID = speakerID;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getAudioLocation() {
        return audioLocation;
    }

    public void setAudioLocation(String audioLocation) {
        this.audioLocation = audioLocation;
    }

    public int getEmotion() {
        return emotion;
    }

    public void setEmotion(int emotion) {
        this.emotion = emotion;
    }
}
