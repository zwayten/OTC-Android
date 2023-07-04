package com.example.orangetrainingcenterandroid.data.mapper

import com.example.orangetrainingcenterandroid.data.model.TokenApiModel
import com.example.orangetrainingcenterandroid.domain.login.model.TokenDomainModel

object TokenMapper {
    fun mapToDomain(tokenDataModel: TokenApiModel): TokenDomainModel {
        return TokenDomainModel(
            token = tokenDataModel.token
        )
    }
}