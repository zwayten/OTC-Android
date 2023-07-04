package com.example.orangetrainingcenterandroid.domain.login.usecase

import com.example.orangetrainingcenterandroid.data.login.remote.dto.reset_password.ResetPasswordResponse
import com.example.orangetrainingcenterandroid.domain.login.ResetPasswordRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(private val resetPasswordRepository: ResetPasswordRepository) {
    suspend fun resetPassword(email: Map<String, String>) : ResetPasswordResponse {
        return resetPasswordRepository.resetPassword(email)
    }
}