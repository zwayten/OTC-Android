package com.example.orangetrainingcenterandroid.domain.poll.model

data class PollChoice(
    val choice: String,
    val votes: Int
)
data class PollDomainModel(
    val _id: String,
    val title: String,
    val description: String,
    val choices: List<PollChoice>,
    val hasVoted: Boolean = false
)
