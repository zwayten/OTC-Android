package com.example.orangetrainingcenterandroid.domain.training.usecase

import com.example.orangetrainingcenterandroid.domain.training.TrainingRepository
import com.example.orangetrainingcenterandroid.domain.training.model.TrainerProfileDomainModel
import javax.inject.Inject

class GetTrainerProfileUseCase  @Inject constructor(private val trainingRepository: TrainingRepository) {
suspend fun getTrainerProfile(id:String): TrainerProfileDomainModel{
    return trainingRepository.getTrainerProfile(id);
}
}