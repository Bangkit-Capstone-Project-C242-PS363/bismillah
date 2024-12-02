package com.adira.signmaster.data.retrofit

import com.adira.signmaster.data.response.LoginResponse
import com.adira.signmaster.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiServiceLogin {

    @FormUrlEncoded
    @POST("auth/register")
    fun registerUser(
        @Body registerResponse: RegisterResponse
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Body loginResponse: LoginResponse
    ): Call<LoginResponse>
}
