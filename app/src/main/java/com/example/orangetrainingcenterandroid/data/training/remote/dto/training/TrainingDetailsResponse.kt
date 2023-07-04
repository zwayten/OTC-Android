package com.example.orangetrainingcenterandroid.data.training.remote.dto.training

import com.example.orangetrainingcenterandroid.data.model.TrainingApiModel
import com.google.gson.annotations.SerializedName

data class TrainingDetailsResponse(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("success")
    var success: Boolean? = true,
    @SerializedName("training") val training: TrainingApiModel)
