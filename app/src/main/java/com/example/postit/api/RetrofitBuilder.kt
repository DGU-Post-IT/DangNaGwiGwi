package com.example.postit.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitBuilder {
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(2,TimeUnit.MINUTES)
        .readTimeout(2,TimeUnit.MINUTES)
        .writeTimeout(2,TimeUnit.MINUTES)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://clovaspeech-gw.ncloud.com/external/v1/2149/b840bcbecb94846c95e2e54fe00107fee4a2e5fbb10ddd53c5a562ddfec02271/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val naverApi = retrofit.create(NaverApi::class.java)
}