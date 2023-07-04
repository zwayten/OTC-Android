package com.example.orangetrainingcenterandroid.domain.login.usecase

import android.content.SharedPreferences
import com.example.orangetrainingcenterandroid.data.login.remote.dto.ProfileUpdateRequest.ProfileUpdateRequest
import com.example.orangetrainingcenterandroid.data.login.remote.dto.profile.UserProfileResponse
import com.example.orangetrainingcenterandroid.domain.login.UserProfileRepository
import javax.inject.Inject
import javax.inject.Named

class UpdateProfileUseCase @Inject constructor(private val userProfileRepository: UserProfileRepository, @Named("TOKEN") private val sharedPreferences: SharedPreferences) {

    suspend fun updateProfile(profileUpdateRequest: ProfileUpdateRequest): UserProfileResponse? {
        val token = sharedPreferences.getString("TOKEN", null)
        return token?.let { userProfileRepository.updateProfile(it,profileUpdateRequest) }
    }

}