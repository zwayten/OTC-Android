package com.example.orangetrainingcenterandroid.domain.training.usecase

import android.content.SharedPreferences
import com.example.orangetrainingcenterandroid.domain.training.TrainingRepository
import javax.inject.Inject
import javax.inject.Named

class RequestParticipationUseCase @Inject constructor(private val trainingRepository: TrainingRepository, @Named("TOKEN") private val sharedPreferences: SharedPreferences) {

    suspend fun requestParticipation(id: String) {
        val token = sharedPreferences.getString("TOKEN", null)
        return token.let {
            if (it != null) {

                trainingRepository.requestParticipation(it,id)
            }
        }
    }
}