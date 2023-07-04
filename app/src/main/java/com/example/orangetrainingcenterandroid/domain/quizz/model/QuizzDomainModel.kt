package com.example.orangetrainingcenterandroid.domain.quizz.model

data class AnswerDomain(
    val optionText: String,
    val isCorrect: Boolean,
    val answersIndex: Int,
    val _id: String
)

data class QuestionDomain(
    val questionNumber: Int,
    val questionText: String,
    val answers: List<AnswerDomain>,
    val _id: String
)

data class QuizzDomainModel(
    val _id: String,
    val type: String,
    val description: String,
    val questions: List<QuestionDomain>
)