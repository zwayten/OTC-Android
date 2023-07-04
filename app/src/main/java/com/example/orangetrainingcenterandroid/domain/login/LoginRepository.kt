package com.example.orangetrainingcenterandroid.domain.login

import com.example.orangetrainingcenterandroid.data.login.remote.dto.LoginRequest
import com.example.orangetrainingcenterandroid.data.model.TokenApiModel
import com.example.orangetrainingcenterandroid.data.model.TokenValidity
import com.example.orangetrainingcenterandroid.domain.login.model.LoginResult

interface LoginRepository {
    suspend fun login(loginRequest: LoginRequest) : LoginResult

     fun checkNotificationPermission(): String?
     fun handleNotificationPermission(permission: String)
    fun saveToken(token: String)
    fun clearToken()

    fun saveUser(id:String)
    suspend fun checkTokenValidity(): TokenValidity?

}