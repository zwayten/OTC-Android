package com.example.orangetrainingcenterandroid.data.poll.remote.dto

import com.example.orangetrainingcenterandroid.data.model.PollApiModel
import com.google.gson.annotations.SerializedName

data class PollResponse(
    @SerializedName("poll")
    val poll: PollApiModel
)
