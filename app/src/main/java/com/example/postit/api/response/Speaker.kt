package com.example.postit.api.response


import com.google.gson.annotations.SerializedName

data class Speaker(
    @SerializedName("edited")
    val edited: Boolean?,
    @SerializedName("label")
    val label: String?,
    @SerializedName("name")
    val name: String?
)