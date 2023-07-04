package com.example.orangetrainingcenterandroid.data.training.remote.dto.training

import com.example.orangetrainingcenterandroid.data.model.LightTrainingApiModel
import com.google.gson.annotations.SerializedName

data class TrainingResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("trainings")
    val trainings: List<LightTrainingApiModel>,
    @SerializedName("nextCursor")
    val nextCursor: String?
)