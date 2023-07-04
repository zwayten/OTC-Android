package com.example.orangetrainingcenterandroid.data.mapper

import com.example.orangetrainingcenterandroid.data.model.TrainerProfileApi
import com.example.orangetrainingcenterandroid.domain.training.model.TrainerProfileDomainModel

object TrainerProfileMapper {

    fun mapToDomain(trainerProfileApi: TrainerProfileApi): TrainerProfileDomainModel {
        return TrainerProfileDomainModel(
            _id = trainerProfileApi._id,
            email = trainerProfileApi.email,
            fullName = trainerProfileApi.fullName,
            phoneNumber = trainerProfileApi.phoneNumber,
            direction = trainerProfileApi.direction,
            gender = trainerProfileApi.gender,
            role = trainerProfileApi.role,
            skills = trainerProfileApi.skills,
            departement = trainerProfileApi.departement
        )
    }

}