package com.example.orangetrainingcenterandroid.domain.training.model

import java.util.*

data class TrainingDomainModel(
    val _id: String,
    val title: String,
    val description: String,
    val startDate: Date,
    val endDate: Date,
    val theme: String,
    val onLine: Boolean,
    val isAsynchronous: Boolean,
    val assignedTo: String ="",
    val duration:String = "",
    val cover: String,
)
