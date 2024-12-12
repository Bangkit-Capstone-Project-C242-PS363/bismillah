package com.adira.signmaster.ui.quiz

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.adira.signmaster.data.model.Chapter
import com.adira.signmaster.data.model.CompleteChapterRequest
import com.adira.signmaster.data.model.CompleteChapterResponse
import com.adira.signmaster.data.model.Quiz
import com.adira.signmaster.data.repository.QuizRepository
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = QuizRepository(application)

    private val _quiz = MutableLiveData<List<Quiz>>()
    val quiz: LiveData<List<Quiz>> get() = _quiz

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _certificateUrl = MutableLiveData<String?>()
    val certificateUrl: LiveData<String?> get() = _certificateUrl

    private val _chapters = MutableLiveData<List<Chapter>>(emptyList())
    val chapters: LiveData<List<Chapter>> get() = _chapters

    fun markChapterAsCompleted(chapterId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.completeChapter(CompleteChapterRequest(chapterId))
                if (response?.error == false) {
                    val updatedChapters = _chapters.value.orEmpty().map { chapter ->
                        if (chapter.id == chapterId) chapter.copy(completed = true) else chapter
                    }
                    _chapters.value = updatedChapters
                } else {
                    Log.e("QuizViewModel", "Failed to mark chapter as completed: ${response?.message}")
                }
            } catch (e: Exception) {
                Log.e("QuizViewModel", "Error marking chapter as completed: ${e.message}")
            }
        }
    }

    fun resetAllChaptersCompletion() {
        viewModelScope.launch {
            val isSuccess = repository.resetAllChaptersCompletion()

            if (isSuccess) {
                val resetChapters = _chapters.value.orEmpty().map { chapter ->
                    chapter.copy(completed = false)
                }
                _chapters.value = resetChapters
                Log.d("QuizViewModel", "All chapters reset successfully.")
            } else {
                Log.e("QuizViewModel", "Failed to reset all chapters.")
            }
        }
    }



    fun fetchChapters() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.fetchChapters()
                if (response?.error == false) {
                    _chapters.value = response.data
                    _certificateUrl.value = response.certificate_url
                    Log.d("QuizViewModel", "Fetched chapters: ${response.data.map { it.id }}")
                } else {
                    _error.value = response?.message ?: "Unknown error"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }


    fun fetchQuiz(chapterId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.fetchQuiz(chapterId)
                if (response?.error == false) {
                    _quiz.value = response.data
                } else {
                    _error.value = response?.message ?: "Unknown error"
                }
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("QuizViewModel", "Error fetching quizzes: ${e.message}")
            }
        }
    }
    suspend fun completeChapter(request: CompleteChapterRequest): CompleteChapterResponse? {
        return try {
            val response = repository.completeChapter(request)
            if (response?.error == false) {
                Log.d("QuizViewModel", "Chapter ${request.chapter_id} marked as completed successfully")
            } else {
                Log.e("QuizViewModel", "Failed to mark chapter as completed: ${response?.message}")
            }
            response
        } catch (e: Exception) {
            Log.e("QuizViewModel", "Error completing chapter: ${e.message}")
            null
        }
    }

    fun isChapterIdValid(chapterId: Int): Boolean {
        val chapterExists = _chapters.value.orEmpty().any { it.id == chapterId }
        Log.d("QuizViewModel", "Validating chapter_id: $chapterId, exists: $chapterExists")
        return chapterExists
    }

}