package com.example.orangetrainingcenterandroid.data.login.remote.dto.password_update_request

import com.google.gson.annotations.SerializedName

data class PasswordUpdateResponse(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("success")
    var success: Boolean? = true,
)
