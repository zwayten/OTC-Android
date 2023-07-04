package com.example.orangetrainingcenterandroid.data.poll.remote.dto

import com.google.gson.annotations.SerializedName


data class VoteResponse(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("success")
    var success: Boolean? = true,
)
