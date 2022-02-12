package com.example.postit.api.response


import com.google.gson.annotations.SerializedName

data class ClovaResponse(
    @SerializedName("confidence")
    val confidence: Double?,
    @SerializedName("keywords")
    val keywords: Keywords?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("params")
    val params: Params?,
    @SerializedName("progress")
    val progress: Int?,
    @SerializedName("result")
    val result: String?,
    @SerializedName("segments")
    val segments: List<Segment>?,
    @SerializedName("speakers")
    val speakers: List<SpeakerX>?,
    @SerializedName("text")
    val text: String?,
    @SerializedName("token")
    val token: String?,
    @SerializedName("version")
    val version: String?
)