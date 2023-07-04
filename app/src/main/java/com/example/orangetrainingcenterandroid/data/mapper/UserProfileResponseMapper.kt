package com.example.orangetrainingcenterandroid.data.mapper

import com.example.orangetrainingcenterandroid.data.login.remote.dto.profile.UserProfileResponse
import com.example.orangetrainingcenterandroid.domain.login.model.UserProfileResult

object UserProfileResponseMapper {
    fun mapToDomain(userProfileResponse: UserProfileResponse): UserProfileResult {
        val userDomainModel = UserMapper.mapToDomain(userProfileResponse.user)
        return UserProfileResult(userDomainModel, userProfileResponse.message)
    }
}