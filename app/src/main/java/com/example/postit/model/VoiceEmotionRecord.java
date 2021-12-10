package com.example.postit.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class VoiceEmotionRecord {
    String audioUrl;
    int emotion;
    @ServerTimestamp
    Date date;

    public VoiceEmotionRecord() {
    }
    public VoiceEmotionRecord(int emotion) {
        this.emotion = emotion;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public int getEmotion() {
        return emotion;
    }

    public void setEmotion(int emotion) {
        this.emotion = emotion;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
