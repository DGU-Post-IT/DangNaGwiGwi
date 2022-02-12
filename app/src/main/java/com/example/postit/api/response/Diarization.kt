package com.example.postit.api.response


import com.google.gson.annotations.SerializedName

data class Diarization(
    @SerializedName("enable")
    val enable: Boolean?,
    @SerializedName("speakerCountMax")
    val speakerCountMax: Int?,
    @SerializedName("speakerCountMin")
    val speakerCountMin: Int?
)