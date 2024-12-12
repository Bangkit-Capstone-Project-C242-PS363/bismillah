package com.adira.signmaster.data.retrofit

import com.adira.signmaster.data.model.ChapterResponse
import com.adira.signmaster.data.model.QuizResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import com.adira.signmaster.data.model.CompleteChapterRequest
import com.adira.signmaster.data.model.CompleteChapterResponse


interface ApiServiceQuiz {
    @POST("chapters/reset-all")
    suspend fun resetAllChapters(): Response<CompleteChapterResponse>
    @GET("getquizz/{chapterId}")
    suspend fun getQuiz(@Path("chapterId") chapterId: Int): Response<QuizResponse>

    @GET("getchapters")
    suspend fun getChaptersWithToken(): Response<ChapterResponse>

    @POST("complete")
    suspend fun completeChapter(@Body requestBody: CompleteChapterRequest): Response<CompleteChapterResponse>
}
