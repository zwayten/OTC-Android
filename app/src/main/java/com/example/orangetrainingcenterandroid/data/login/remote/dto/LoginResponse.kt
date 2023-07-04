package com.example.orangetrainingcenterandroid.data.login.remote.dto

import com.example.orangetrainingcenterandroid.data.model.TokenApiModel
import com.example.orangetrainingcenterandroid.data.model.UserApiModel
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("user")
    val user: UserApiModel,
    @SerializedName("token")
    val token: String
)
