package com.example.orangetrainingcenterandroid.data.mapper

import com.example.orangetrainingcenterandroid.data.login.remote.dto.LoginResponse
import com.example.orangetrainingcenterandroid.domain.login.model.LoginResult
import com.example.orangetrainingcenterandroid.domain.login.model.TokenDomainModel

object LoginResponseMapper {
    fun mapToDomain(loginResponse: LoginResponse): LoginResult {
        val userDomainModel = UserMapper.mapToDomain(loginResponse.user)
        val tokenDomainModel = TokenDomainModel(loginResponse.token)
        return LoginResult(userDomainModel, tokenDomainModel)
    }
}
