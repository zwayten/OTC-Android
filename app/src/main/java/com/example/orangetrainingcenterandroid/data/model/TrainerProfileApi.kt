package com.example.orangetrainingcenterandroid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrainerProfileApi(
    @SerialName("_id")
    val _id: String,
    @SerialName("email")
    val email: String,
    @SerialName("fullName")
    val fullName: String,
    @SerialName("phoneNumber")
    val phoneNumber: String,
    @SerialName("direction")
    val direction: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("role")
    val role: String,
    @SerialName("skills")
    val skills: List<String>,
    @SerialName("departement")
    val departement: String
)
