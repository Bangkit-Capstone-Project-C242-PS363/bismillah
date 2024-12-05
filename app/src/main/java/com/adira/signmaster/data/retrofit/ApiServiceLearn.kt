package com.adira.signmaster.data.retrofit

import com.adira.signmaster.data.response.LearnResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiServiceLearn {
    @GET("materials/getchapters")
    suspend fun getChapters(): Response<LearnResponse>
}