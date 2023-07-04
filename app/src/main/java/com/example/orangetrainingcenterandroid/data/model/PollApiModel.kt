package com.example.orangetrainingcenterandroid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PollChoice(
    val choice: String,
    val votes: Float
)


@Serializable
data class PollApiModel(

    @SerialName("_id")
    val _id: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("choices")
    val choices: List<PollChoice>,
    @SerialName("hasVoted")
    val hasVoted: Boolean = false
)
