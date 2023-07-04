package com.example.orangetrainingcenterandroid.data.mapper

import com.example.orangetrainingcenterandroid.data.model.UserApiModel
import com.example.orangetrainingcenterandroid.domain.login.model.UserDomainModel

object UserMapper {
    fun mapToDomain(userModel: UserApiModel): UserDomainModel {
        return UserDomainModel(
            _id = userModel._id,
            email = userModel.email,
            fullName = userModel.fullName,
            phoneNumber = userModel.phoneNumber,
           // photo = userModel.photo,
            direction = userModel.direction,
            gender = userModel.gender,
            role = userModel.role,
            departement = userModel.departement,
        )
    }
}
