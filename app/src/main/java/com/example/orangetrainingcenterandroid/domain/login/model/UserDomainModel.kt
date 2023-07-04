package com.example.orangetrainingcenterandroid.domain.login.model

data class UserDomainModel(
    val _id: String,
    val email: String,
    val fullName: String,
    val phoneNumber: String,
    //val photo: String,
    val direction: String,
    val gender: String,
    val role: String,
    val departement: String,
)
