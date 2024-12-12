package com.adira.signmaster.data.repository

import android.util.Log
import android.content.Context
import com.adira.signmaster.data.model.ChapterResponse
import com.adira.signmaster.data.model.CompleteChapterRequest
import com.adira.signmaster.data.model.CompleteChapterResponse
import com.adira.signmaster.data.model.QuizResponse
import com.adira.signmaster.data.pref.UserPreference
import com.adira.signmaster.data.pref.dataStore
import com.adira.signmaster.data.retrofit.ApiConfigQuiz
import kotlinx.coroutines.flow.first

class QuizRepository(private val context: Context) {

    private val api = ApiConfigQuiz.retrofit

    suspend fun fetchChapters(): ChapterResponse? {
        return try {
            // 1. Get the token from UserPreference
            val token = UserPreference.getInstance(context.dataStore).getLoginToken().first()

            // Log the token for debugging purposes
            Log.d("QuizRepository", "Bearer Token: $token")

            // 2. Get the API service with the token
            val apiService = ApiConfigQuiz.getApiServiceQuizWithToken(token)

            // 3. Call the getChaptersWithToken endpoint
            val response = apiService.getChaptersWithToken()

            // 4. Handle success response
            if (response.isSuccessful) {
                Log.d("QuizRepository", "Chapters fetched successfully: ${response.body()}")
                response.body()
            } else {
                // Log the error response from the server
                Log.e("QuizRepository", "Failed to fetch chapters: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("QuizRepository", "Error fetching chapters: ${e.message}")
            null
        }
    }



    suspend fun fetchQuiz(chapterId: Int): QuizResponse? {
        val response = api.getQuiz(chapterId)
        return if (response.isSuccessful) {
            response.body()?.also {
                Log.d("QuizRepository", "Quiz fetched successfully: ${it.data}")
            }
        } else {
            Log.e("QuizRepository", "Failed to fetch quiz: ${response.errorBody()?.string()}")
            null
        }
    }

    suspend fun completeChapter(request: CompleteChapterRequest): CompleteChapterResponse? {
        return try {
            // 1. Get the token from UserPreference
            val token = UserPreference.getInstance(context.dataStore).getLoginToken().first()

            // 2. Get the API service with the token
            val apiService = ApiConfigQuiz.getApiServiceQuizWithToken(token)

            // 3. Call the completeChapter endpoint
            val response = apiService.completeChapter(request)

            // 4. Handle success response
            if (response.isSuccessful) {
                Log.d("QuizRepository", "Chapter completed successfully: ${response.body()}")
                response.body()
            } else {
                Log.e("QuizRepository", "Failed to complete chapter: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("QuizRepository", "Error completing chapter: ${e.message}")
            null
        }
    }
    suspend fun resetAllChaptersCompletion(): Boolean {
        return try {
            val token = UserPreference.getInstance(context.dataStore).getLoginToken().first()
            val apiService = ApiConfigQuiz.getApiServiceQuizWithToken(token)

            // **Panggil reset API untuk semua chapter**
            val response = apiService.resetAllChapters()

            if (response.isSuccessful) {
                Log.d("QuizRepository", "All chapters reset successfully.")
                true
            } else {
                Log.e("QuizRepository", "Failed to reset all chapters: ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e("QuizRepository", "Error resetting all chapters: ${e.message}")
            false
        }
    }


}
