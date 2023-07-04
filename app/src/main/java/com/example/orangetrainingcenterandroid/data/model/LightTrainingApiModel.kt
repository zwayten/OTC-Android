package com.example.orangetrainingcenterandroid.data.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SerialName("training")
data class LightTrainingApiModel(
    @SerialName("_id")
    val _id: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @Contextual
    @SerialName("startDate")
    val startDate: Date,
    @Contextual
    @SerialName("endDate")
    val endDate: Date,
    @SerialName("theme")
    val theme: String,
    @SerialName("onLine")
    val onLine: Boolean,
    @SerialName("isAsynchronous")
    val isAsynchronous: Boolean,
    @SerialName("duration")
    val duration: String,
    @SerialName("assignedTo")
    val assignedTo: String,
    @SerialName("cover")
    val cover: String
)
