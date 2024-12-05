package com.adira.signmaster.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfigLearn {
    private const val BASE_URL = "https://signmaster-material-quiz-304278585381.asia-southeast2.run.app/"

    val apiServiceLearn: ApiServiceLearn by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServiceLearn::class.java)
    }
}

