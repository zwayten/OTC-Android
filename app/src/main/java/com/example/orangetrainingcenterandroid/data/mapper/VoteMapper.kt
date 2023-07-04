package com.example.orangetrainingcenterandroid.data.mapper

import com.example.orangetrainingcenterandroid.data.quizz.remote.dto.quizz.AnswersRequest
import com.example.orangetrainingcenterandroid.data.quizz.remote.dto.quizz.VoteRequest
import com.example.orangetrainingcenterandroid.data.quizz.remote.dto.quizz.VoteResponse
import com.example.orangetrainingcenterandroid.domain.quizz.model.VoteDomainModel

object VoteMapper {
    fun mapToDomain(response: VoteResponse): VoteDomainModel {
        return VoteDomainModel(
            score = response.score
        )
    }
    fun mapToRequest(domainModel: VoteDomainModel, questionId: String): VoteRequest {
        val answersRequest = AnswersRequest(
            questionId = questionId,
            selectedOptions = listOf(domainModel.score)
        )
        return VoteRequest(answers = listOf(answersRequest))
    }
}