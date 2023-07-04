package com.example.orangetrainingcenterandroid.data.mapper



import com.example.orangetrainingcenterandroid.data.training.remote.dto.training.PresenceStateResponse
import com.example.orangetrainingcenterandroid.domain.training.model.PresenceDomainModel
import com.example.orangetrainingcenterandroid.domain.training.model.PresenceStateDomainModel


object PresenceStateMapper {
    fun mapToDomain(responseModel: PresenceStateResponse): PresenceStateDomainModel {
        val presenceStateList = responseModel.presenceState.map { apiModel ->
            PresenceDomainModel(
                dayNumber = apiModel.dayNumber,
                attended = apiModel.attended,
                isCurrentDay = apiModel.isCurrentDay
            )
        }
        return PresenceStateDomainModel(presenceStateList)
    }
}