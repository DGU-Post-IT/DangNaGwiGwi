package com.example.postit.api

import com.example.postit.api.response.ClovaResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface NaverApi {
    @POST("recognizer/url")
    fun getSTT(@Body clovaSttBody: ClovaSttBody):Call<ClovaResponse>
}