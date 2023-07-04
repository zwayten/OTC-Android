package com.example.orangetrainingcenterandroid.domain.login.usecase

import com.example.orangetrainingcenterandroid.data.login.remote.dto.LoginRequest
import com.example.orangetrainingcenterandroid.data.login.remote.dto.LoginResponse
import com.example.orangetrainingcenterandroid.domain.login.LoginRepository
import com.example.orangetrainingcenterandroid.domain.login.model.LoginResult
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val loginRepository: LoginRepository) {

    suspend fun login(email: String, password: String): LoginResult {
        val loginRequest = LoginRequest(email, password)


        return loginRepository.login(loginRequest)
    }
}