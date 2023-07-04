package com.example.orangetrainingcenterandroid.presentation.util

data class Session(
    val token: String,
    val expiresAt: Long,
    val isLoggedIn: Boolean,
)

