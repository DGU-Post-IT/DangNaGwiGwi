package com.example.postit.api.response


import com.google.gson.annotations.SerializedName

data class Params(
    @SerializedName("boostings")
    val boostings: List<Any>?,
    @SerializedName("completion")
    val completion: String?,
    @SerializedName("diarization")
    val diarization: Diarization?,
    @SerializedName("domain")
    val domain: String?,
    @SerializedName("forbiddens")
    val forbiddens: String?,
    @SerializedName("fullText")
    val fullText: Boolean?,
    @SerializedName("lang")
    val lang: String?,
    @SerializedName("priority")
    val priority: Int?,
    @SerializedName("resultToObs")
    val resultToObs: Boolean?,
    @SerializedName("service")
    val service: String?,
    @SerializedName("userdata")
    val userdata: Userdata?,
    @SerializedName("wordAlignment")
    val wordAlignment: Boolean?
)