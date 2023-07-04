package com.example.orangetrainingcenterandroid.domain.quizz.usecase

import com.example.orangetrainingcenterandroid.domain.quizz.QuizzRepository
import com.example.orangetrainingcenterandroid.domain.quizz.model.QuizzDomainModel
import javax.inject.Inject

class GetQuizByTrainingUseCase @Inject constructor(private val quizzRepository: QuizzRepository){
    suspend fun getQuizByTraining(id: String): QuizzDomainModel?{
        return quizzRepository.getQuizByTraining(id)
    }
}