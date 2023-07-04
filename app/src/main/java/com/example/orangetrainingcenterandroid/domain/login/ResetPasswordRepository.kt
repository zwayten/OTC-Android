package com.example.orangetrainingcenterandroid.domain.login

import com.example.orangetrainingcenterandroid.data.login.remote.dto.reset_password.ResetPasswordResponse

interface ResetPasswordRepository {
    suspend fun resetPassword(email: Map<String, String>): ResetPasswordResponse
}