package com.example.orangetrainingcenterandroid.data.quizz.remote.api

import com.example.orangetrainingcenterandroid.data.quizz.remote.dto.quizz.HasVotedResponse
import com.example.orangetrainingcenterandroid.data.quizz.remote.dto.quizz.QuizzResponse
import com.example.orangetrainingcenterandroid.data.quizz.remote.dto.quizz.VoteRequest
import com.example.orangetrainingcenterandroid.data.quizz.remote.dto.quizz.VoteResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface QuizzApi {
    @GET("get-quiz/{id}")
    suspend fun getQuizByTraining(@Path("id") id: String, @Header("Authorization") token: String): Response<QuizzResponse>

    @GET("animation/lastvisible")
    suspend fun getLastAnimationQuizz(@Header("Authorization") token: String): Response<QuizzResponse>

    @POST("quiz/{id}/attempt")
    suspend fun voteOnQuiz(@Path("id") id: String, @Header("Authorization") token: String, @Body voteRequest: VoteRequest): Response<VoteResponse>

    @GET("hasvoted/{id}")
    suspend fun hasVoted(@Path("id") id: String, @Header("Authorization") token: String): Response<HasVotedResponse>
    }