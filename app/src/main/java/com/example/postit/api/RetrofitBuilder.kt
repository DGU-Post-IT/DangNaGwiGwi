package com.example.postit.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://clovaspeech-gw.ncloud.com/external/v1/2149/b840bcbecb94846c95e2e54fe00107fee4a2e5fbb10ddd53c5a562ddfec02271/")
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val naverApi = retrofit.create(NaverApi::class.java)
}