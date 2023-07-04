package com.example.orangetrainingcenterandroid.domain.login.usecase


import com.example.orangetrainingcenterandroid.data.login.remote.dto.sendRequest.SendRequest
import com.example.orangetrainingcenterandroid.domain.login.UserProfileRepository
import javax.inject.Inject


class SendRequestUseCase @Inject constructor(private val userProfileRepository: UserProfileRepository) {

    suspend fun sendReqeust(sendRequest: SendRequest): String{
    val response = userProfileRepository.sendRequest(sendRequest)
        return response.message.toString()
    }
}