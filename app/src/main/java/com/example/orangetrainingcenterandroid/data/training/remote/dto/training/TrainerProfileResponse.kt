package com.example.orangetrainingcenterandroid.data.training.remote.dto.training

import com.example.orangetrainingcenterandroid.data.model.TrainerProfileApi
import com.google.gson.annotations.SerializedName

data class TrainerProfileResponse (
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("success")
    var success: Boolean? = true,
    @SerializedName("trainer")
    var trainer: TrainerProfileApi,
)