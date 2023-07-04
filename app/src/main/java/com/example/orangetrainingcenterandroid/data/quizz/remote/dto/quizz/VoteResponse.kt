package com.example.orangetrainingcenterandroid.data.quizz.remote.dto.quizz

import com.google.gson.annotations.SerializedName

data class VoteResponse(
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("success")
    var success: Boolean? = true,
    @SerializedName("score") val score: Int
)
