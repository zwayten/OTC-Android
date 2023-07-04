package com.example.orangetrainingcenterandroid.domain.training.usecase

import com.example.orangetrainingcenterandroid.domain.training.TrainingRepository
import com.example.orangetrainingcenterandroid.domain.training.model.TrainingHomeSectionsDomainModel
import javax.inject.Inject

class GetTrainingsHomeSectionUseCase @Inject constructor(private val trainingRepository: TrainingRepository) {
   suspend fun getTrainingsHomeSection(): TrainingHomeSectionsDomainModel{
       return trainingRepository.getTrainingsHomeSection()
   }
}