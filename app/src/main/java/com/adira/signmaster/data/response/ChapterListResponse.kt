package com.adira.signmaster.data.response

data class LearnResponse(
	val error: Boolean,
	val message: String,
	val data: List<LearnChapter>
)

data class LearnChapter(
	val id: Int,
	val title: String,
	val icon_url: String
)




