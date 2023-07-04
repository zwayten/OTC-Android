package com.example.orangetrainingcenterandroid.data.poll.repository

import android.content.SharedPreferences
import com.example.orangetrainingcenterandroid.common.ApiException
import com.example.orangetrainingcenterandroid.data.mapper.PollMapper
import com.example.orangetrainingcenterandroid.data.mapper.TrainingMapper
import com.example.orangetrainingcenterandroid.data.poll.remote.api.PollApi
import com.example.orangetrainingcenterandroid.data.poll.remote.dto.VoteRequest
import com.example.orangetrainingcenterandroid.data.poll.remote.dto.VoteResponse
import com.example.orangetrainingcenterandroid.domain.poll.PollRepository
import com.example.orangetrainingcenterandroid.domain.poll.model.PollDomainModel
import com.example.orangetrainingcenterandroid.domain.training.model.TrainingResult
import javax.inject.Inject
import javax.inject.Named

class PollRepositoryImpl @Inject constructor(private val pollapi: PollApi, @Named("TOKEN") private val sharedPreferences: SharedPreferences): PollRepository {
    override suspend fun getPoll(): PollDomainModel {
        val token = sharedPreferences.getString("TOKEN", null)
        val response = token?.let { pollapi.getLastVisiblePoll(it) }

        if (response != null && response.isSuccessful) {
            val pollResponse = response.body()
            if (pollResponse != null ) {

                return PollMapper.mapToDomain(pollResponse)
            } else {
                throw ApiException("Empty or unsuccessful response")
            }
        } else {
            throw ApiException(response?.message() ?: "Unknown error")
        }
    }



    override suspend fun votePoll(voteRequest: VoteRequest, id: String): VoteResponse? {
        try {
            val token = sharedPreferences.getString("TOKEN", null)
            val response = token?.let { pollapi.votePoll(it, voteRequest, id) }
            return response
        } catch (e: Exception) {
            throw ApiException("Error voting in poll: ${e.message}")
        }
    }


}