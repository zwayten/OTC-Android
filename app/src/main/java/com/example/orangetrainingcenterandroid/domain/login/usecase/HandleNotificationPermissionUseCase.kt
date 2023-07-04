package com.example.orangetrainingcenterandroid.domain.login.usecase

import com.example.orangetrainingcenterandroid.domain.login.LoginRepository
import javax.inject.Inject

class HandleNotificationPermissionUseCase  @Inject constructor(private val loginRepository: LoginRepository) {

    fun handleNotificationPermission(permission: Boolean){
        if(permission){
            loginRepository.handleNotificationPermission("ENABLED_NOTIFICATION")
        } else {
            loginRepository.handleNotificationPermission("DISABLED_NOTIFICATION")
        }

    }
}