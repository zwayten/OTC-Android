package com.example.orangetrainingcenterandroid.data.mapper

import com.example.orangetrainingcenterandroid.data.model.LightTrainingApiModel
import com.example.orangetrainingcenterandroid.data.training.remote.dto.training.TrainingResponse
import com.example.orangetrainingcenterandroid.domain.training.model.TrainingDomainModel
import com.example.orangetrainingcenterandroid.domain.training.model.TrainingResult
import java.time.LocalDate
import java.util.*


object TrainingMapper {

    fun mapToDomain(trainingModel: TrainingResponse): TrainingResult {
        return TrainingResult(
            trainings = trainingModel.trainings.map { mapToDomainModel(it) },
            nextCursor = trainingModel.nextCursor
        )
    }

    fun mapToDomainModel(lightTrainingApiModel: LightTrainingApiModel): TrainingDomainModel {
        return TrainingDomainModel(
            _id = lightTrainingApiModel._id,
            title = lightTrainingApiModel.title,
            description = lightTrainingApiModel.description,
            startDate = lightTrainingApiModel.startDate,
            endDate = lightTrainingApiModel.endDate,
            theme = lightTrainingApiModel.theme ?: "",
            onLine = lightTrainingApiModel.onLine ?: false,
            isAsynchronous = lightTrainingApiModel.isAsynchronous,
            duration = lightTrainingApiModel.duration ?: "",
            assignedTo = lightTrainingApiModel.assignedTo ?: "",
            cover = lightTrainingApiModel.cover
        )
    }
}

