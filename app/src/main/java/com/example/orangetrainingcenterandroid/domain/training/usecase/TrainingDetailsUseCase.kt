package com.example.orangetrainingcenterandroid.domain.training.usecase

import android.content.SharedPreferences
import com.example.orangetrainingcenterandroid.data.training.remote.dto.training.TrainingDetailsResponse
import com.example.orangetrainingcenterandroid.domain.training.TrainingRepository
import com.example.orangetrainingcenterandroid.domain.training.model.TrainingDetailsDomainModel
import javax.inject.Inject
import javax.inject.Named

class TrainingDetailsUseCase @Inject constructor(private val trainingRepository: TrainingRepository) {
    suspend fun trainingDetails(id: String): TrainingDetailsDomainModel {

        return trainingRepository.TrainingDetails(id)
    }
}