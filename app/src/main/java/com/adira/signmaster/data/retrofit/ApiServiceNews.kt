package com.adira.signmaster.data.retrofit

import com.adira.signmaster.data.response.News
import retrofit2.Call
import retrofit2.http.*

interface ApiServiceNews {

    @GET("news")
    fun getAllNews(): Call<List<News>>

    @GET("news/{id}")
    fun getNewsById(@Path("id") id: Int): Call<News>

    @POST("news")
    fun createNews(
        @Body news: News
    ): Call<News>

    @PUT("news/{id}")
    fun updateNews(
        @Path("id") id: Int,
        @Body news: News
    ): Call<News>

    @DELETE("news/{id}")
    fun deleteNews(
        @Path("id") id: Int
    ): Call<Void>
}