package com.example.orangetrainingcenterandroid.domain.quizz.usecase

import com.example.orangetrainingcenterandroid.data.quizz.remote.dto.quizz.VoteRequest
import com.example.orangetrainingcenterandroid.domain.quizz.QuizzRepository
import com.example.orangetrainingcenterandroid.domain.quizz.model.VoteDomainModel
import javax.inject.Inject

class VoteOnQuizzUseCase @Inject constructor(private val quizzRepository: QuizzRepository){
    suspend fun voteOnQuizz(id: String, voteRequest: VoteRequest): VoteDomainModel?{
        val response = quizzRepository.voteOnQuizz(id,voteRequest)
        return response
    }
}