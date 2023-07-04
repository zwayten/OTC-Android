package com.example.orangetrainingcenterandroid.data.quizz.repository

import android.content.SharedPreferences
import com.example.orangetrainingcenterandroid.common.ApiException
import com.example.orangetrainingcenterandroid.data.mapper.QuizzMapper
import com.example.orangetrainingcenterandroid.data.mapper.VoteMapper
import com.example.orangetrainingcenterandroid.data.quizz.remote.api.QuizzApi
import com.example.orangetrainingcenterandroid.data.quizz.remote.dto.quizz.HasVotedResponse
import com.example.orangetrainingcenterandroid.data.quizz.remote.dto.quizz.VoteRequest

import com.example.orangetrainingcenterandroid.domain.quizz.QuizzRepository
import com.example.orangetrainingcenterandroid.domain.quizz.model.QuizzDomainModel
import com.example.orangetrainingcenterandroid.domain.quizz.model.VoteDomainModel
import javax.inject.Inject
import javax.inject.Named

class QuizzRepositoryImpl @Inject constructor(
    private val quizzApi: QuizzApi,
    @Named("TOKEN") private val sharedPreferences: SharedPreferences
) : QuizzRepository {

    override suspend fun getQuizByTraining(id: String): QuizzDomainModel? {
        try {
            val token = sharedPreferences.getString("TOKEN", null)
            val response = token?.let { quizzApi.getQuizByTraining(id, it) }


            if (response?.isSuccessful == true) {
                val quizzApiModel = response.body()?.quizz
                return quizzApiModel?.let { QuizzMapper.mapToDomain(it) }
            } else {
                throw ApiException("Error in fetching Quizz: ${response?.message()}")
            }
        } catch (e: Exception) {
            throw ApiException("Error in fetching Quizz: ${e.message}")
        }
    }

    override suspend fun getLastAnimationQuizz(): QuizzDomainModel? {
        try {
            val token = sharedPreferences.getString("TOKEN", null)
            val response = token?.let { quizzApi.getLastAnimationQuizz(it) }

            if(response?.isSuccessful ==  true){
                val fetchedQuiz = response.body()?.quizz

                return fetchedQuiz?.let { QuizzMapper.mapToDomain(it) }
            }

        } catch (e: Exception) {
            throw ApiException("Error in fetching Quizz: ${e.message}")
        }
        return null
    }

    override suspend fun voteOnQuizz(id: String,voteRequest: VoteRequest): VoteDomainModel? {
        try {
            val token = sharedPreferences.getString("TOKEN", null)
            val response = token?.let { quizzApi.voteOnQuiz(id, it,voteRequest) }
            if (response?.isSuccessful == true) {
                val voteResponse = response.body()
                return voteResponse?.let { VoteMapper.mapToDomain(it) }
            } else {
                throw ApiException("Error while voting on Quizz: ${response?.message()}")
            }
        }
        catch (e: Exception) {
            throw ApiException("Error while voting Quizz: ${e.message}")
        }
    }

    override suspend fun hasVoted(id: String): Boolean {
        try{
            val token = sharedPreferences.getString("TOKEN", null)
            val response = token?.let { quizzApi.hasVoted(id, it) }
            if (response?.isSuccessful == true) {
                val response = response.body()?.hasVoted

                return response!!
            } else {
                throw ApiException("Error while voting on Quizz: ${response?.message()}")
            }
        } catch(e: Exception) {
            throw ApiException("Error while voting Quizz: ${e.message}")
        }
    }
}