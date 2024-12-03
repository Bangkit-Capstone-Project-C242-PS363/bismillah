package com.adira.signmaster.data.response

data class LearnResponse(
	val data: List<DataItem?>? = null,
	val error: Boolean? = null,
	val message: String? = null
)

data class DataItem(
	val iconUrl: String? = null,
	val id: Int? = null,
	val title: String? = null
)

