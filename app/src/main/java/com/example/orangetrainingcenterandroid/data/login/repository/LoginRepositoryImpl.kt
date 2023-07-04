package com.example.orangetrainingcenterandroid.data.login.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.orangetrainingcenterandroid.common.ApiException
import com.example.orangetrainingcenterandroid.data.login.remote.api.LoginApi
import com.example.orangetrainingcenterandroid.data.login.remote.dto.LoginRequest
import com.example.orangetrainingcenterandroid.data.mapper.LoginResponseMapper
import com.example.orangetrainingcenterandroid.data.mapper.TrainingDetailsMapper
import com.example.orangetrainingcenterandroid.data.model.TokenApiModel
import com.example.orangetrainingcenterandroid.data.model.TokenValidity
import com.example.orangetrainingcenterandroid.domain.login.LoginRepository
import com.example.orangetrainingcenterandroid.domain.login.model.LoginResult
import javax.inject.Inject
import javax.inject.Named

class LoginRepositoryImpl @Inject constructor(
    private val loginApi: LoginApi,
    @Named("TOKEN") private val sharedPreferences: SharedPreferences,
    @Named("NOTIFICATION") private val notificationSharedPreferences: SharedPreferences,
    @Named("USER") private val userSharedPreferences: SharedPreferences) : LoginRepository {

    override suspend fun login(loginRequest: LoginRequest): LoginResult {
        val response = loginApi.login(loginRequest)
        if (response.isSuccessful) {
            val loginResponse = response.body() ?: throw ApiException("Empty response body")
            val token = loginResponse.token
            saveToken(token)
            saveUser(loginResponse.user._id)

            return LoginResponseMapper.mapToDomain(loginResponse)
        } else {
            throw ApiException(response.message())
        }
    }

    override  fun checkNotificationPermission(): String? {
        return notificationSharedPreferences.getString("NOTIFICATION_PERMISSION_FLAG", "NO_FLAG")
    }

    override fun handleNotificationPermission(permission: String) {
        notificationSharedPreferences.edit {
            putString("NOTIFICATION_PERMISSION_FLAG", permission)
            apply()
        }
    }

    override fun saveToken(token: String) {
        with(sharedPreferences.edit()) {
            putString("TOKEN", token)
            putBoolean("isLoggedIn",true)
            apply()
        }
    }

    override fun clearToken() {
        with(sharedPreferences.edit()) {
            putString("TOKEN", "")
            putBoolean("isLoggedIn",false)
            apply()
        }
    }

    override fun saveUser(id: String) {
        with(userSharedPreferences.edit()) {
            putString("USER", id)
            apply()
        }
    }

    override suspend fun checkTokenValidity(): TokenValidity? {
        val token = sharedPreferences.getString("TOKEN", null)
        val response = token?.let { loginApi.checkTokenValidity(it) }
        if (response != null && response.isSuccessful) {
            return response.body()

        } else {
            throw ApiException(response?.message() ?: "Unknown error")
        }
    }

}