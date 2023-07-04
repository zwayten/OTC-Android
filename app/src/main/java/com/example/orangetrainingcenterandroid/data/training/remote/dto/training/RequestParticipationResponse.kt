package com.example.orangetrainingcenterandroid.data.training.remote.dto.training

import com.google.gson.annotations.SerializedName

data class RequestParticipationResponse(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("success")
    var success: Boolean? = true,
)
