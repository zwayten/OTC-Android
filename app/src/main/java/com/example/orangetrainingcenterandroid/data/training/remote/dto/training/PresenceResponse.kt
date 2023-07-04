package com.example.orangetrainingcenterandroid.data.training.remote.dto.training

import com.google.gson.annotations.SerializedName

data class PresenceResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
)
