package com.example.orangetrainingcenterandroid.domain.training.model

data class TrainingHomeSectionsDomainModel(
    val thisWeekTrainings : List<TrainingDomainModel>,
    val targetedTrainings : List<TrainingDomainModel>,
    val upcomingTrainings : List<TrainingDomainModel>
)
