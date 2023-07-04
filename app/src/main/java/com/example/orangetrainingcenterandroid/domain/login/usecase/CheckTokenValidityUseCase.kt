package com.example.orangetrainingcenterandroid.domain.login.usecase

import com.example.orangetrainingcenterandroid.data.model.TokenValidity
import com.example.orangetrainingcenterandroid.domain.login.LoginRepository
import javax.inject.Inject

class CheckTokenValidityUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun checkTokenValidity():TokenValidity?{
        return loginRepository.checkTokenValidity()
    }
}