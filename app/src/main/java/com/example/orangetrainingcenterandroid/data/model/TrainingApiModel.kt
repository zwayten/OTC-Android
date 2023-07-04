package com.example.orangetrainingcenterandroid.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date
import kotlinx.serialization.Contextual


@Serializable
data class Planing(
    val daysNumber: Int,
    val startingHour: String,
    val endingHour: String,
    val dailyContent: List<String>
)
@Serializable
data class AssignedTo(
    val _id: String,
    val fullName: String
)

@Serializable
data class TrainingApiModel(
    @SerialName("_id")
    val _id: String,
    @SerialName("title")
    val title: String,
    @SerialName("capacity")
    val capacity: Int,
    @SerialName("onLine")
    val onLine: Boolean,
    @SerialName("isAsynchronous")
    val isAsynchronous: Boolean,
    @SerialName("description")
    val description: String,
    @SerialName("location")
    val location: String = "",
    @Contextual
    @SerialName("startDate")
    val startDate: Date,
    @Contextual
    @SerialName("endDate")
    val endDate: Date,
    @SerialName("planing")
    val planing: List<Planing>,
    @SerialName("isQuizzPreEvaluation")
    val isQuizzPreEvaluation: Boolean?,
    @SerialName("isQuizzEvaluation")
    val isQuizzEvaluation: Boolean?,
    @SerialName("cover")
    val cover: String,
    @SerialName("theme")
    val theme: ThemeApiModel,
    @SerialName("duration")
    val duration: String,
    @SerialName("quizzPreEvaluation")
    val quizzPreEvaluation: String?,
    @SerialName("quizzEvaluation")
    val quizzEvaluation: String?,
    @SerialName("activateQuizEvaluation")
    val activateQuizEvaluation: Boolean,
    @SerialName("assignedTo")
    val assignedTo: AssignedTo,
    @SerialName("link")
    val link: String,
    @SerialName("goals")
    val goals: List<String>
)
