package com.example.orangetrainingcenterandroid.domain.login.usecase


import com.example.orangetrainingcenterandroid.domain.login.UserProfileRepository
import com.example.orangetrainingcenterandroid.domain.login.model.UserProfileResult
import javax.inject.Inject



class UserProfileUseCase @Inject constructor(private val userProfileRepository: UserProfileRepository) {

    suspend fun fetchProfile(): UserProfileResult {

        return userProfileRepository.fetchUserProfile()
    }
}