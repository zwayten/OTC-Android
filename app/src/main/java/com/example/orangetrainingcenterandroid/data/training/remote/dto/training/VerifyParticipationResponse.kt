package com.example.orangetrainingcenterandroid.data.training.remote.dto.training

data class VerifyParticipationResponse(
    val success: Boolean,
    val message: String,
    val pending: Boolean,
    val accepted: Boolean
)
