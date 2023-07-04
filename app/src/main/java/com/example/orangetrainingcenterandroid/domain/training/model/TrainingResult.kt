package com.example.orangetrainingcenterandroid.domain.training.model

data class TrainingResult(
    val trainings: List<TrainingDomainModel>,
    val nextCursor: String?

)
