package com.example.orangetrainingcenterandroid.data.mapper

import com.example.orangetrainingcenterandroid.data.model.ThemeApiModel
import com.example.orangetrainingcenterandroid.domain.training.model.ThemeDomainModel


object ThemeMapper {
    fun mapToDomain(themeResponse: ThemeApiModel): ThemeDomainModel {
       return ThemeDomainModel(themeResponse._id, themeResponse.name)
    }
}