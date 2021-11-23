package com.example.postit.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class VoiceEmotionRecord {
    String audioUrl;
    Long emotion;
    @ServerTimestamp
    Date date;

    public VoiceEmotionRecord() {
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public Long getEmotion() {
        return emotion;
    }

    public void setEmotion(Long emotion) {
        this.emotion = emotion;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
