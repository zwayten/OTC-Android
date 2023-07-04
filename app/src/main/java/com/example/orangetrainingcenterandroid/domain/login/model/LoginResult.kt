package com.example.orangetrainingcenterandroid.domain.login.model

data class LoginResult(
    val user: UserDomainModel,
    val token: TokenDomainModel
)
