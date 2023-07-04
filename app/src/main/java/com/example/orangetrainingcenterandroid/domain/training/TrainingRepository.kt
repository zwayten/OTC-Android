package com.example.orangetrainingcenterandroid.domain.training

import com.example.orangetrainingcenterandroid.data.training.remote.dto.training.RequestParticipationResponse
import com.example.orangetrainingcenterandroid.data.training.remote.dto.training.VerifyParticipationResponse
import com.example.orangetrainingcenterandroid.domain.training.model.*
import java.time.LocalDate



interface TrainingRepository {
    suspend fun fetchAvailableTrainings(searchQuery: String, themeQuery: String, startDateQuery: LocalDate?, endDateQuery: LocalDate?, cursor: String?): TrainingResult
    suspend fun TrainingDetails( id: String): TrainingDetailsDomainModel;
    suspend fun requestParticipation(token: String, id: String): RequestParticipationResponse;
    suspend fun verifyParticipation(token: String, id: String): VerifyParticipationResponse
    suspend fun getTrainerProfile(id:String): TrainerProfileDomainModel
    suspend fun getCurrentTraining(): TrainingDetailsDomainModel?;
    suspend fun getAllThemes(): List<ThemeDomainModel>
    suspend fun confirmAttendance(id:String)
    suspend fun getPresenceState(id:String): PresenceStateDomainModel?

    suspend fun getTrainingsHomeSection():TrainingHomeSectionsDomainModel
}