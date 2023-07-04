package com.example.orangetrainingcenterandroid.data.login.repository


import com.example.orangetrainingcenterandroid.common.ApiException
import com.example.orangetrainingcenterandroid.data.login.remote.api.ResetPasswordApi
import com.example.orangetrainingcenterandroid.data.login.remote.dto.reset_password.ResetPasswordResponse
import com.example.orangetrainingcenterandroid.domain.login.ResetPasswordRepository
import javax.inject.Inject

class ResetPasswordRepositoryImpl @Inject constructor(private val resetPasswordApi: ResetPasswordApi): ResetPasswordRepository {
    override suspend fun resetPassword(email: Map<String, String>): ResetPasswordResponse {
        val response = resetPasswordApi.resetPassword(email);

        if (response.isSuccessful) {

            return response.body() ?: throw ApiException("Empty response body")
        } else {
            throw ApiException(response.message())
        }
    }
}