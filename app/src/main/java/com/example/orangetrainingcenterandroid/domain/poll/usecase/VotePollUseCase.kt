package com.example.orangetrainingcenterandroid.domain.poll.usecase

import com.example.orangetrainingcenterandroid.data.poll.remote.dto.VoteRequest
import com.example.orangetrainingcenterandroid.data.poll.remote.dto.VoteResponse
import com.example.orangetrainingcenterandroid.domain.poll.PollRepository
import javax.inject.Inject

class VotePollUseCase @Inject constructor(private val pollRepository: PollRepository) {
    suspend fun votePoll(voteRequest: VoteRequest,id:String): VoteResponse?{
        return pollRepository.votePoll(voteRequest,id)
    }
}