package com.example.orangetrainingcenterandroid.data.login.repository

import android.content.SharedPreferences
import com.example.orangetrainingcenterandroid.common.ApiException
import com.example.orangetrainingcenterandroid.data.login.remote.api.UserProfileApi
import com.example.orangetrainingcenterandroid.data.login.remote.dto.ProfileUpdateRequest.ProfileUpdateRequest
import com.example.orangetrainingcenterandroid.data.login.remote.dto.password_update_request.PasswordUpdateRequest
import com.example.orangetrainingcenterandroid.data.login.remote.dto.password_update_request.PasswordUpdateResponse
import com.example.orangetrainingcenterandroid.data.login.remote.dto.profile.UserProfileResponse
import com.example.orangetrainingcenterandroid.data.login.remote.dto.sendRequest.SendRequest
import com.example.orangetrainingcenterandroid.data.login.remote.dto.sendRequest.SendRequestResponse
import com.example.orangetrainingcenterandroid.data.mapper.UserProfileResponseMapper
import com.example.orangetrainingcenterandroid.domain.login.UserProfileRepository
import com.example.orangetrainingcenterandroid.domain.login.model.UserProfileResult
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Named

class UserProfileRepositoryImpl @Inject constructor(private val userProfileApi: UserProfileApi, @Named("TOKEN") private val sharedPreferences: SharedPreferences): UserProfileRepository {
    override suspend fun fetchUserProfile(): UserProfileResult {
        val token = sharedPreferences.getString("TOKEN", null)
        val response = token?.let { userProfileApi.fetchUserProfile(it) }

        if (response != null) {
            if (response.isSuccessful) {
                val userProfileResponse = response.body() ?: throw ApiException("Empty response body")
                return UserProfileResponseMapper.mapToDomain(userProfileResponse)
            } else {
                throw ApiException(response.message())
            }
        }
        // Add a default return statement or handle the case when response is null
        throw ApiException("Failed to fetch user profile")
    }

    override suspend fun updateProfile(token: String, profileUpdateRequest: ProfileUpdateRequest): UserProfileResponse {
        val response = userProfileApi.updateUserProfile(token,profileUpdateRequest);
        if (response.isSuccessful) {
            return response.body() ?: throw ApiException("Empty response body")
        } else {
            throw ApiException(response.message())
        }
    }

    override suspend fun updatePassword(token: String, passwordUpdateRequest: PasswordUpdateRequest): PasswordUpdateResponse {
        val response = userProfileApi.updatePassword(token, passwordUpdateRequest)
        if (response.isSuccessful) {
            return response.body() ?: throw ApiException("Empty response body")
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                val errorJson = JSONObject(errorBody)
                errorJson.optString("message", "Unknown error occurred")
            } catch (e: JSONException) {
                "Unknown error occurred"
            }
            throw ApiException(errorMessage)
        }
    }

    override suspend fun sendRequest( sendRequest: SendRequest): SendRequestResponse {
        val token = sharedPreferences.getString("TOKEN", null)
        val response = token?.let { userProfileApi.sendRequest(it, sendRequest) }

        if (response != null && response.isSuccessful) {
                return response.body() ?: throw ApiException("Empty response body")
            } else {
                val errorBody = response?.errorBody()?.string()
                val errorMessage = try {
                    val errorJson = JSONObject(errorBody)
                    errorJson.optString("message", "Unknown error occurred")
                } catch (e: JSONException) {
                    "Unknown error occurred"
                }
                throw ApiException(errorMessage)
            }
        }

}