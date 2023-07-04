package com.example.orangetrainingcenterandroid.domain.quizz

import com.example.orangetrainingcenterandroid.data.quizz.remote.dto.quizz.HasVotedResponse
import com.example.orangetrainingcenterandroid.data.quizz.remote.dto.quizz.VoteRequest
import com.example.orangetrainingcenterandroid.domain.quizz.model.QuizzDomainModel
import com.example.orangetrainingcenterandroid.domain.quizz.model.VoteDomainModel

interface QuizzRepository {
    suspend fun getQuizByTraining(id:String): QuizzDomainModel?
    suspend fun getLastAnimationQuizz():QuizzDomainModel?
    suspend fun voteOnQuizz(id:String, voteRequest:VoteRequest): VoteDomainModel?

    suspend fun hasVoted(id:String): Boolean
}