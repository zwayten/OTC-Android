package com.example.orangetrainingcenterandroid.domain.login.usecase

import com.example.orangetrainingcenterandroid.domain.login.LoginRepository
import javax.inject.Inject

class CheckNotificationPermissionUseCase @Inject constructor(private val loginRepository: LoginRepository) {

     fun checkNotificationPermission() : String? {
        val permission = loginRepository.checkNotificationPermission()
        return permission
    }
}