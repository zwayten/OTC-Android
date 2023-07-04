package com.example.orangetrainingcenterandroid.domain.login.usecase

import com.example.orangetrainingcenterandroid.domain.login.LoginRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(private val loginRepository: LoginRepository) {
     fun logout(){
        loginRepository.clearToken()
    }
}