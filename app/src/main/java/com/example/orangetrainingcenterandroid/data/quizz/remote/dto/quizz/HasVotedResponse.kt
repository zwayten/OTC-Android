package com.example.orangetrainingcenterandroid.data.quizz.remote.dto.quizz

import com.google.gson.annotations.SerializedName

data class HasVotedResponse(

    @SerializedName("message")
    var message: String? = "",
    @SerializedName("success")
    var success: Boolean? = true,
    @SerializedName("hasVoted") val hasVoted: Boolean
)
