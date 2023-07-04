package com.example.orangetrainingcenterandroid.domain.login

import com.example.orangetrainingcenterandroid.data.login.remote.dto.ProfileUpdateRequest.ProfileUpdateRequest
import com.example.orangetrainingcenterandroid.data.login.remote.dto.password_update_request.PasswordUpdateRequest
import com.example.orangetrainingcenterandroid.data.login.remote.dto.password_update_request.PasswordUpdateResponse
import com.example.orangetrainingcenterandroid.data.login.remote.dto.profile.UserProfileResponse
import com.example.orangetrainingcenterandroid.data.login.remote.dto.sendRequest.SendRequest
import com.example.orangetrainingcenterandroid.data.login.remote.dto.sendRequest.SendRequestResponse
import com.example.orangetrainingcenterandroid.domain.login.model.UserProfileResult

interface UserProfileRepository {
    suspend fun fetchUserProfile(): UserProfileResult

    suspend fun updateProfile(token: String, profileUpdateRequest: ProfileUpdateRequest): UserProfileResponse

    suspend fun updatePassword(token: String, passwordUpdateRequest: PasswordUpdateRequest): PasswordUpdateResponse

    suspend fun sendRequest(sendRequest:SendRequest ): SendRequestResponse
}