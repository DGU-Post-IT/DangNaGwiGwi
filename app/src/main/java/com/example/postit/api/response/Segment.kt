package com.example.postit.api.response


import com.google.gson.annotations.SerializedName

data class Segment(
    @SerializedName("confidence")
    val confidence: Double?,
    @SerializedName("diarization")
    val diarization: DiarizationX?,
    @SerializedName("end")
    val end: Int?,
    @SerializedName("speaker")
    val speaker: Speaker?,
    @SerializedName("start")
    val start: Int?,
    @SerializedName("text")
    val text: String?,
    @SerializedName("textEdited")
    val textEdited: String?,
    @SerializedName("words")
    val words: List<List<Any>>?
)