package com.example.orangetrainingcenterandroid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresenceStateApiModel(
    @SerialName("dayNumber")
    val dayNumber: Int,
    @SerialName("attended")
    val attended: Boolean,
    @SerialName("isCurrentDay")
    val isCurrentDay: Boolean
)
