package com.example.orangetrainingcenterandroid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ThemeApiModel(
    @SerialName("_id")
    val _id: String,
    @SerialName("name")
    val name: String,
)
