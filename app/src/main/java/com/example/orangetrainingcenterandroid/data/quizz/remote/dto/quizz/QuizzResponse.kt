package com.example.orangetrainingcenterandroid.data.quizz.remote.dto.quizz

import com.example.orangetrainingcenterandroid.data.model.QuizzApiModel
import com.google.gson.annotations.SerializedName

class QuizzResponse (
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("success")
    var success: Boolean? = true,
    @SerializedName("quizz") val quizz: QuizzApiModel
)
