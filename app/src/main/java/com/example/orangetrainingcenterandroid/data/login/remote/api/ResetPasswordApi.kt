package com.example.orangetrainingcenterandroid.data.login.remote.api


import com.example.orangetrainingcenterandroid.data.login.remote.dto.reset_password.ResetPasswordResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ResetPasswordApi {

    @POST("auth/forgot-password")
    suspend fun resetPassword(@Body email: Map<String, String>) : Response<ResetPasswordResponse>
}