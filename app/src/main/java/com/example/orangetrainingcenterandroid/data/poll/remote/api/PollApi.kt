package com.example.orangetrainingcenterandroid.data.poll.remote.api


import com.example.orangetrainingcenterandroid.data.poll.remote.dto.PollResponse
import com.example.orangetrainingcenterandroid.data.poll.remote.dto.VoteRequest
import com.example.orangetrainingcenterandroid.data.poll.remote.dto.VoteResponse
import com.example.orangetrainingcenterandroid.data.training.remote.dto.training.RequestParticipationResponse
import retrofit2.Response
import retrofit2.http.*

interface PollApi {

    @GET("android/poll/{id}")
    suspend fun getPoll(@Path("id") id: String, @Header("Authorization") token: String): Response<PollResponse>

    @GET("poll-last-visible")
    suspend fun getLastVisiblePoll(@Header("Authorization") token: String): Response<PollResponse>

    @POST("insert-vote/{id}")
    suspend fun votePoll(@Header("Authorization") token: String, @Body voteRequest: VoteRequest,@Path("id") id: String) : VoteResponse

}