package com.example.orangetrainingcenterandroid.domain.training.usecase

import com.example.orangetrainingcenterandroid.domain.training.TrainingRepository
import com.example.orangetrainingcenterandroid.domain.training.model.PresenceStateDomainModel
import javax.inject.Inject

class GetPresenceStateUseCase  @Inject constructor(private val trainingRepository: TrainingRepository) {

    suspend fun getPresenceState(id:String): PresenceStateDomainModel? {
        return trainingRepository.getPresenceState(id);
    }
}