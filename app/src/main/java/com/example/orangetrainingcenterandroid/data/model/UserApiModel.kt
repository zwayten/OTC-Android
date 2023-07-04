package com.example.orangetrainingcenterandroid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserApiModel(
    @SerialName("_id")
    val _id: String,
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
    @SerialName("fullName")
    val fullName: String,
    @SerialName("phoneNumber")
    val phoneNumber: String,
    //@SerialName("photo")
    //val photo: String,
    @SerialName("direction")
    val direction: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("role")
    val role: String,
    @SerialName("departement")
    val departement: String

)
