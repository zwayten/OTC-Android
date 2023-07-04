package com.example.orangetrainingcenterandroid.domain.training.usecase

import com.example.orangetrainingcenterandroid.domain.training.TrainingRepository
import com.example.orangetrainingcenterandroid.domain.training.model.TrainingDetailsDomainModel
import javax.inject.Inject

class GetCurrentTrainingUseCase @Inject constructor(private val trainingRepository: TrainingRepository) {
suspend fun getCurrentTraining(): TrainingDetailsDomainModel? {
    val response = trainingRepository.getCurrentTraining();
    return response;
}
}