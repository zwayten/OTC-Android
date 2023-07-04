package com.example.orangetrainingcenterandroid.domain.quizz.usecase

import com.example.orangetrainingcenterandroid.domain.quizz.QuizzRepository
import com.example.orangetrainingcenterandroid.domain.quizz.model.QuizzDomainModel
import javax.inject.Inject

class GetLastAnimationQuizzUseCase @Inject constructor(private val quizzRepository: QuizzRepository){

    suspend fun getLastAnimationQuizz(): QuizzDomainModel?{
        return quizzRepository.getLastAnimationQuizz()
    }

    }