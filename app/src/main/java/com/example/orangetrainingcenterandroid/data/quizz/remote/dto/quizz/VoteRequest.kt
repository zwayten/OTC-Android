package com.example.orangetrainingcenterandroid.data.quizz.remote.dto.quizz

import com.google.gson.annotations.SerializedName


data class AnswersRequest(
    @SerializedName("questionId")
    val questionId: String,
    @SerializedName("selectedOptions")
    val selectedOptions: List<Int>,
)

data class VoteRequest(
    @SerializedName("answers") val answers: List<AnswersRequest>
)
