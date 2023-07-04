package com.example.orangetrainingcenterandroid.domain.poll.usecase

import com.example.orangetrainingcenterandroid.domain.poll.PollRepository
import com.example.orangetrainingcenterandroid.domain.poll.model.PollDomainModel

import javax.inject.Inject

class GetPollUseCase @Inject constructor(private val pollRepository: PollRepository) {

    suspend fun getPoll(): PollDomainModel {

        return pollRepository.getPoll()
    }
}