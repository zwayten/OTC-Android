package com.example.orangetrainingcenterandroid.presentation.settings


import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.orangetrainingcenterandroid.domain.login.usecase.LogoutUseCase
import com.example.orangetrainingcenterandroid.presentation.util.SessionCache

import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val logoutUseCase: LogoutUseCase, private val sessionCache: SessionCache) : ViewModel() {

    fun logout() {
        logoutUseCase.logout()
        sessionCache.clearSession()
    }
}