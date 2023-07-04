package com.example.orangetrainingcenterandroid.data.poll.remote.dto

import com.google.gson.annotations.SerializedName

data class VoteRequest(
    @SerializedName("choix") val choix: Int,
)
