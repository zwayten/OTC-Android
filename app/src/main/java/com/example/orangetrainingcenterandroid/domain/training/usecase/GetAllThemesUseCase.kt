package com.example.orangetrainingcenterandroid.domain.training.usecase

import com.example.orangetrainingcenterandroid.data.training.remote.dto.training.ThemeResponse
import com.example.orangetrainingcenterandroid.domain.training.TrainingRepository
import com.example.orangetrainingcenterandroid.domain.training.model.ThemeDomainModel
import javax.inject.Inject

class GetAllThemesUseCase @Inject constructor(private val trainingRepository: TrainingRepository) {
    suspend fun getAllThemes() : List<ThemeDomainModel>{
        return trainingRepository.getAllThemes();

    }
}