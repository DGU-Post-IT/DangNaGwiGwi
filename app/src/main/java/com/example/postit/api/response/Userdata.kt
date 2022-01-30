package com.example.postit.api.response


import com.google.gson.annotations.SerializedName

data class Userdata(
    @SerializedName("_ncp_DomainCode")
    val ncpDomainCode: String?,
    @SerializedName("_ncp_DomainId")
    val ncpDomainId: Int?,
    @SerializedName("_ncp_TaskId")
    val ncpTaskId: Int?,
    @SerializedName("_ncp_TraceId")
    val ncpTraceId: String?
)