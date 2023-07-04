package com.example.orangetrainingcenterandroid.domain.quizz.usecase

import com.example.orangetrainingcenterandroid.domain.quizz.QuizzRepository
import javax.inject.Inject

class HasVotedUseCase @Inject constructor(private val quizzRepository: QuizzRepository){
    suspend fun hasVote(id:String): Boolean{
       val response = quizzRepository.hasVoted(id);
        return response
    }
}