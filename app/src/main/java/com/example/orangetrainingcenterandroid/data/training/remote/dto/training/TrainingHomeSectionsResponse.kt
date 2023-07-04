package com.example.orangetrainingcenterandroid.data.training.remote.dto.training

import com.example.orangetrainingcenterandroid.data.model.LightTrainingApiModel
import com.google.gson.annotations.SerializedName

data class TrainingHomeSectionsResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("thisWeekTrainings")
    val thisWeekTrainings: List<LightTrainingApiModel>,
    @SerializedName("targetedTrainings")
    val targetedTrainings: List<LightTrainingApiModel>,
    @SerializedName("upcomingTrainings")
    val upcomingTrainings: List<LightTrainingApiModel>,
)
