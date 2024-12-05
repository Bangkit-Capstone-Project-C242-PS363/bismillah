package com.adira.signmaster.ui.study

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.adira.signmaster.data.response.Chapter
import com.adira.signmaster.data.retrofit.ApiConfigLearn
import kotlinx.coroutines.Dispatchers

class StudyViewModel : ViewModel() {

    fun fetchChapters() = liveData(Dispatchers.IO) {
        try {
            val response = ApiConfigLearn.apiServiceLearn.getChapters()
            if (response.isSuccessful) {
                val chapterResponse = response.body()
                chapterResponse?.data?.let {
                    emit(it)
                }
            } else {
                emit(emptyList<Chapter>())
            }
        } catch (e: Exception) {
            emit(emptyList<Chapter>())
        }
    }
}