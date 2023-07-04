package com.example.orangetrainingcenterandroid.data.login.remote.api

import com.example.orangetrainingcenterandroid.data.login.remote.dto.ProfileUpdateRequest.ProfileUpdateRequest
import com.example.orangetrainingcenterandroid.data.login.remote.dto.password_update_request.PasswordUpdateRequest
import com.example.orangetrainingcenterandroid.data.login.remote.dto.password_update_request.PasswordUpdateResponse
import com.example.orangetrainingcenterandroid.data.login.remote.dto.profile.UserProfileResponse
import com.example.orangetrainingcenterandroid.data.login.remote.dto.sendRequest.SendRequest
import com.example.orangetrainingcenterandroid.data.login.remote.dto.sendRequest.SendRequestResponse
import com.example.orangetrainingcenterandroid.data.model.UserApiModel
import retrofit2.Response
import retrofit2.http.*

interface UserProfileApi {
    @GET("user/me")
    suspend fun fetchUserProfile(@Header("Authorization") token: String) : Response<UserProfileResponse>

    @PUT("user/me/update")
    suspend fun updateUserProfile(@Header("Authorization") token: String, @Body profileUpdateRequest: ProfileUpdateRequest) : Response<UserProfileResponse>

    @PUT("user/me/update/password")
    suspend fun updatePassword(@Header("Authorization") token: String, @Body passwordUpdateRequest: PasswordUpdateRequest) : Response<PasswordUpdateResponse>

    @POST("insert-request")
    suspend fun sendRequest(@Header("Authorization") token: String, @Body sendRequest: SendRequest) : Response<SendRequestResponse>
}
