package com.example.orangetrainingcenterandroid.data.training.remote.dto.training

import com.example.orangetrainingcenterandroid.data.model.PresenceStateApiModel
import com.example.orangetrainingcenterandroid.data.model.ThemeApiModel
import com.google.gson.annotations.SerializedName

data class PresenceStateResponse(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("success")
    var success: Boolean? = true,
    @SerializedName("presenceState") val presenceState: List<PresenceStateApiModel>
)
