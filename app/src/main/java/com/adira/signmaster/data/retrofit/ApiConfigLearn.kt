package com.adira.signmaster.data.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfigLearn {
    private const val BASE_URL = "https://signmaster-material-quiz-kji5w4ybbq-et.a.run.app/materials/"

    val instance: ApiServiceLearn by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build())
            .build()
            .create(ApiServiceLearn::class.java)
    }
}




