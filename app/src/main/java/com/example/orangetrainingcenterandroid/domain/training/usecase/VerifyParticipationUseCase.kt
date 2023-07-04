package com.example.orangetrainingcenterandroid.domain.training.usecase

import android.content.SharedPreferences
import com.example.orangetrainingcenterandroid.data.training.remote.dto.training.TrainingDetailsResponse
import com.example.orangetrainingcenterandroid.data.training.remote.dto.training.VerifyParticipationResponse
import com.example.orangetrainingcenterandroid.domain.training.TrainingRepository
import javax.inject.Inject
import javax.inject.Named

class VerifyParticipationUseCase @Inject constructor(private val trainingRepository: TrainingRepository, @Named("TOKEN") private val sharedPreferences: SharedPreferences) {

    suspend fun verifyParticipation(id: String): VerifyParticipationResponse? {
        val token = sharedPreferences.getString("TOKEN", null)
        return token?.let { trainingRepository.verifyParticipation(it,id) }
    }
}