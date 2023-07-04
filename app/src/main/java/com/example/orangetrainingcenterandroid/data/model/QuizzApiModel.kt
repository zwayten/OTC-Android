package com.example.orangetrainingcenterandroid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnswersApi(
    @SerialName("option_text")
    val option_text: String,
    @SerialName("is_correct")
    val is_correct: Boolean,
    @SerialName("answers_index")
    val answers_index: Int,
    @SerialName("_id")
    val _id: String
)

@Serializable
data class QuestionsApi(
    @SerialName("question_number")
    val question_number: Int,
    @SerialName("question_text")
    val question_text: String,
    @SerialName("answers")
    val answers: List<AnswersApi>,
    @SerialName("_id")
    val _id: String
)

@Serializable
data class QuizzApiModel(
    @SerialName("_id")
    val _id: String,
    @SerialName("type")
    val type: String,
    @SerialName("description")
    val description: String,
    @SerialName("questions")
    val questions: List<QuestionsApi>
)