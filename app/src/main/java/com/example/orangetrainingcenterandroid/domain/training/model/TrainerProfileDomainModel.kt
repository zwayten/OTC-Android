package com.example.orangetrainingcenterandroid.domain.training.model


data class TrainerProfileDomainModel(
    val _id: String,
    val email: String,
    val fullName: String,
    val phoneNumber: String,
    val direction: String,
    val gender: String,
    val role: String,
    val skills: List<String>,
    val departement: String
)
