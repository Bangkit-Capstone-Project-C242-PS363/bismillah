package com.adira.signmaster.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

    data class Chapter(
        val id: Int,
        val title: String,
        val icon_url: String,
        val locked: Boolean,
        val completed: Boolean
    )

    data class ChapterResponse(
        val error: Boolean,
        val message: String,
        val certificate_url: String?, // Nullable because the URL is only available when all chapters are complete
        val data: List<Chapter>
    )


    @Parcelize
    data class Quiz(
        val id: Int,
        val question: String,
        val answers: List<Answer>,
        val correctAnswerIndex: Int
    ) : Parcelable

    @Parcelize
    data class Answer(
        val id: String,
        val answer: String
    ) : Parcelable


    data class QuizResponse(
        val error: Boolean,
        val message: String,
        val data: List<Quiz>
    )

data class CompleteChapterRequest(
    val chapter_id: Int
)

    data class CompleteChapterResponse(
        val error: Boolean,
        val message: String,
        val certificate_url: String? // Nullable because the URL is only available when all chapters are complete
    )
