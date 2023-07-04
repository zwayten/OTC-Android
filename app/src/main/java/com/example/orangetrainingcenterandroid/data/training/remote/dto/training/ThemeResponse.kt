package com.example.orangetrainingcenterandroid.data.training.remote.dto.training

import com.example.orangetrainingcenterandroid.data.model.ThemeApiModel
import com.google.gson.annotations.SerializedName

data class ThemeResponse(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("success")
    var success: Boolean? = true,
    @SerializedName("themes") val themes: List<ThemeApiModel>
)
