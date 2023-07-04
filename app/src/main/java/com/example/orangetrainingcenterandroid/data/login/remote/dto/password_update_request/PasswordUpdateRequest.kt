package com.example.orangetrainingcenterandroid.data.login.remote.dto.password_update_request

import com.google.gson.annotations.SerializedName

data class PasswordUpdateRequest(
    @SerializedName("currentPassword") val currentPassword: String,
    @SerializedName("newPassword") val newPassword: String,
)
