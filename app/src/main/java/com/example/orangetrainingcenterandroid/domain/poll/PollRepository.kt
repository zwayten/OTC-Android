package com.example.orangetrainingcenterandroid.domain.poll

import com.example.orangetrainingcenterandroid.data.poll.remote.dto.VoteRequest
import com.example.orangetrainingcenterandroid.data.poll.remote.dto.VoteResponse
import com.example.orangetrainingcenterandroid.domain.poll.model.PollDomainModel

interface PollRepository {
    suspend fun getPoll(): PollDomainModel
    suspend fun votePoll(voteRequest: VoteRequest,id:String): VoteResponse?
}