package com.example.orangetrainingcenterandroid.data.login.remote.api

import com.example.orangetrainingcenterandroid.data.login.remote.dto.LoginRequest
import com.example.orangetrainingcenterandroid.data.login.remote.dto.LoginResponse
import com.example.orangetrainingcenterandroid.data.model.TokenValidity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginApi {

    //Login
    @POST("auth/signIn")
    suspend fun login(@Body loginRequest: LoginRequest) : Response<LoginResponse>

    @GET("check/token/validity")
    suspend fun checkTokenValidity(@Header("Authorization") token: String) : Response<TokenValidity>


}