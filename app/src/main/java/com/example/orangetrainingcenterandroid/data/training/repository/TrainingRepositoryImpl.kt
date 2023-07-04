package com.example.orangetrainingcenterandroid.data.training.repository

import android.content.SharedPreferences
import com.example.orangetrainingcenterandroid.common.ApiException
import com.example.orangetrainingcenterandroid.data.mapper.*
import com.example.orangetrainingcenterandroid.data.training.remote.api.TrainingApi
import com.example.orangetrainingcenterandroid.data.training.remote.dto.training.RequestParticipationResponse
import com.example.orangetrainingcenterandroid.data.training.remote.dto.training.VerifyParticipationResponse
import com.example.orangetrainingcenterandroid.domain.training.TrainingRepository
import com.example.orangetrainingcenterandroid.domain.training.model.*

import org.json.JSONObject
import java.time.LocalDate
import java.util.*
import javax.inject.Inject
import javax.inject.Named


class TrainingRepositoryImpl @Inject constructor(private val trainingApi: TrainingApi, @Named("TOKEN") private val sharedPreferences: SharedPreferences): TrainingRepository {



    override suspend fun TrainingDetails( id: String): TrainingDetailsDomainModel {
        val token = sharedPreferences.getString("TOKEN", null)
        val response = token?.let { trainingApi.getTrainingDetail(id, it) }
        if (response != null && response.isSuccessful) {
            val trainingApiModel = response.body()?.training
            if (trainingApiModel != null) {
                return TrainingDetailsMapper.mapToDomain(trainingApiModel)
            } else {
                throw ApiException("Empty response body")
            }
        } else {
            throw ApiException(response?.message() ?: "Unknown error")
        }
    }

    override suspend fun requestParticipation(token: String, id: String): RequestParticipationResponse {
       try{
           val response = trainingApi.submitRequestReservation(token, id)
           return response
       } catch (error: ApiException){
           throw ApiException("Empty response body")
       }
    }

    override suspend fun verifyParticipation(token: String, id: String): VerifyParticipationResponse {
        try {
            val response = trainingApi.verifyParticipation(id, token)

            if (response.isSuccessful) {
                val responseBody = response.body()?.string()
                val jsonObject = JSONObject(responseBody)

                val pending = jsonObject.optBoolean("pending", false)
                val accepted = jsonObject.optBoolean("accepted", false)
                val message = jsonObject.optString("message", "")

                return VerifyParticipationResponse(true, message, pending, accepted)
            } else {
                throw ApiException(response.message())
            }
        } catch (e: Exception) {
            throw ApiException(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun getTrainerProfile(id: String): TrainerProfileDomainModel {


        val token = sharedPreferences.getString("TOKEN", null)
        val response = token?.let { trainingApi.getTrainerProfile(it, id) }
        if (response != null && response.isSuccessful) {
            val trainerProfile = response.body()?.trainer
            if (trainerProfile != null) {
                return TrainerProfileMapper.mapToDomain(trainerProfile)
            }
        }
        throw Exception("Failed to retrieve trainer profile.")
    }

    override suspend fun getCurrentTraining(): TrainingDetailsDomainModel? {
        try {
            val token = sharedPreferences.getString("TOKEN", null)
            val response = token?.let { trainingApi.getCurrentTraining(it) }
            if(response?.isSuccessful ==  true){
                val fetchedTraining = response.body()?.training


                return fetchedTraining?.let { TrainingDetailsMapper.mapToDomain(it) }
            }

        } catch (e: Exception) {
            throw ApiException("Error in fetching current training: ${e.message}")
        }
        return null
    }


    override suspend fun fetchAvailableTrainings(
        searchQuery: String,
        themeQuery: String,
        startDateQuery: LocalDate?,
        endDateQuery: LocalDate?,
        cursor: String?
    ): TrainingResult {
        val token = sharedPreferences.getString("TOKEN", null)
        val response = token?.let { trainingApi.fetchAvailableTrainings(it, searchQuery,themeQuery,startDateQuery,endDateQuery, cursor) }

        if (response != null && response.isSuccessful) {
            val trainingsResponse = response.body()
            if (trainingsResponse != null && trainingsResponse.success) {

                return  TrainingMapper.mapToDomain(trainingsResponse)
            } else {
                throw ApiException("Empty or unsuccessful response")
            }
        } else {
            throw ApiException(response?.message() ?: "Unknown error")
        }
    }


    override suspend fun getAllThemes(): List<ThemeDomainModel> {
        try {
            val token = sharedPreferences.getString("TOKEN", null)
            val response = token?.let { trainingApi.getAllThemes(it) }

            if (response != null) {
                if (response.isSuccessful) {
                    val themeResponseList = response.body()?.themes

                    if (!themeResponseList.isNullOrEmpty()) {
                        return themeResponseList.map { themeApiModel -> ThemeMapper.mapToDomain(themeApiModel) }
                    } else {
                        throw Exception("No themes found.")
                    }
                } else {
                    throw Exception("Failed to get themes.")
                }
            } else {
                throw Exception("Response is null.")
            }
        } catch (e: Exception) {
            throw Exception("Error getting themes: ${e.message}")
        }
    }

    override suspend fun confirmAttendance(id: String) {
        try{
            val token = sharedPreferences.getString("TOKEN", null)
            val response = token?.let { trainingApi.confirmAttendance(id,it) }

        } catch (e: Exception) {
            throw Exception("Error getting themes: ${e.message}")
        }
    }

    override suspend fun getPresenceState(id: String): PresenceStateDomainModel {
        val token = sharedPreferences.getString("TOKEN", null)
        val response = token?.let { trainingApi.getPresenceState(id,it) }
        if (response != null && response.isSuccessful) {
            val presenceStateResponse = response.body()
            if (presenceStateResponse != null && presenceStateResponse.success == true) {

                return  PresenceStateMapper.mapToDomain(presenceStateResponse)
            } else {
                throw ApiException("Empty or unsuccessful response")
            }
        } else {
            throw ApiException(response?.message() ?: "Unknown error")
        }
    }

    override suspend fun getTrainingsHomeSection(): TrainingHomeSectionsDomainModel {
        val token = sharedPreferences.getString("TOKEN", null)
        val response = token?.let { trainingApi.getTrainingHomeSections(it) }

        if (response != null && response.isSuccessful) {
            val trainingResponse = response.body()

            if (trainingResponse != null) {
                val thisWeekTrainings = trainingResponse.thisWeekTrainings.map { TrainingMapper.mapToDomainModel(it) }
                val targetedTrainings = trainingResponse.targetedTrainings.map { TrainingMapper.mapToDomainModel(it) }
                val upcomingTrainings = trainingResponse.upcomingTrainings.map { TrainingMapper.mapToDomainModel(it) }

                return TrainingHomeSectionsDomainModel(
                    thisWeekTrainings = thisWeekTrainings,
                    targetedTrainings = targetedTrainings,
                    upcomingTrainings = upcomingTrainings
                )
            } else {
                throw ApiException("Response body is null")
            }
        } else {
            throw ApiException(response?.message() ?: "Unknown error")
        }
    }

}