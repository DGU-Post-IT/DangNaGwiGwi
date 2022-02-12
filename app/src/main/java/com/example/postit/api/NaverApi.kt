package com.example.postit.api

import com.example.postit.BuildConfig
import com.example.postit.api.response.ClovaResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface NaverApi {
    @Headers(
                "Content-Type:application/json",
                "X-CLOVASPEECH-API-KEY:${BuildConfig.CLOVA_KEY}"

    )
    @POST("recognizer/url")
    fun getSTT(@Body clovaSttBody: ClovaSttBody):Call<ClovaResponse>
}