package com.example.orangetrainingcenterandroid.data.mapper


import com.example.orangetrainingcenterandroid.domain.training.model.AssignedTo
import com.example.orangetrainingcenterandroid.data.model.TrainingApiModel
import com.example.orangetrainingcenterandroid.domain.training.model.Planing
import com.example.orangetrainingcenterandroid.domain.training.model.TrainingDetailsDomainModel

object TrainingDetailsMapper {
    fun mapToDomain(trainingModel: TrainingApiModel): TrainingDetailsDomainModel {
        val planingList = trainingModel.planing.map { planing ->
            Planing(
                daysNumber = planing.daysNumber,
                startingHour = planing.startingHour,
                endingHour = planing.endingHour,
                dailyContent = planing.dailyContent
            )

        }
        val assignedTo = AssignedTo(
            _id = trainingModel.assignedTo._id ?: "",
            fullName = trainingModel.assignedTo.fullName ?: ""
        )

        return TrainingDetailsDomainModel(
            _id = trainingModel._id,
            title = trainingModel.title ?: "",
            capacity = trainingModel.capacity ?: 0,
            onLine = trainingModel.onLine ?: false,
            isAsynchronous = trainingModel.isAsynchronous,
            description = trainingModel.description ?: "",
            location = trainingModel.location ?: "",
            startDate = trainingModel.startDate,
            endDate = trainingModel.endDate,
            planing = planingList,
            isQuizzPreEvaluation = trainingModel.isQuizzPreEvaluation,
            isQuizzEvaluation = trainingModel.isQuizzEvaluation,
            cover = trainingModel.cover,
            theme = trainingModel.theme,
            duration = trainingModel.duration ?: "",
            quizzPreEvaluation = trainingModel.quizzPreEvaluation,
            quizzEvaluation = trainingModel.quizzEvaluation,
            activateQuizEvaluation = trainingModel.activateQuizEvaluation,
            assignedTo = assignedTo,
            link = trainingModel.link ?: "",
            goals = trainingModel.goals ?: emptyList()
        )
    }
}