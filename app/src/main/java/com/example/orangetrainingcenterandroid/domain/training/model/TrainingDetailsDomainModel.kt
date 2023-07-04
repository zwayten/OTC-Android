package com.example.orangetrainingcenterandroid.domain.training.model

import com.example.orangetrainingcenterandroid.data.model.ThemeApiModel
import java.time.Duration
import java.util.*


data class Planing(
    val daysNumber: Int,
    val startingHour: String,
    val endingHour: String,
    val dailyContent: List<String>
)
data class AssignedTo(
    val _id: String,
    val fullName: String
)

data class TrainingDetailsDomainModel(
    val _id: String,
    val title: String,
    val capacity: Int,
    val onLine: Boolean,
    val isAsynchronous: Boolean,
    val description: String,
    val location: String = "",
    val startDate: Date,
    val endDate: Date,
    val planing: List<Planing>,
    val isQuizzPreEvaluation: Boolean?,
    val isQuizzEvaluation: Boolean?,
    val cover: String?,
    val theme: ThemeApiModel,
    val duration: String,
    val quizzPreEvaluation: String?,
    val quizzEvaluation: String?,
    val activateQuizEvaluation: Boolean,
    val assignedTo: AssignedTo,
    val link: String,
    val goals: List<String>
)
