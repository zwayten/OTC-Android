package com.example.orangetrainingcenterandroid.domain.training.usecase

import com.example.orangetrainingcenterandroid.domain.training.TrainingRepository
import javax.inject.Inject

class ConfirmAttendanceUseCase @Inject constructor(private val trainingRepository: TrainingRepository,
) {
    suspend fun confirmAttendance(id:String){
        trainingRepository.confirmAttendance(id);
    }
}