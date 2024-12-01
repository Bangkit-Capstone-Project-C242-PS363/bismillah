package com.adira.signmaster.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class RegisterResponse(
	val password: String? = null,
	val confirmPassword: String? = null,
	val email: String? = null,
	val username: String? = null
) : Parcelable
