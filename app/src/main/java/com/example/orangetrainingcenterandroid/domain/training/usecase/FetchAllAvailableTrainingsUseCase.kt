package com.example.orangetrainingcenterandroid.domain.training.usecase

import com.example.orangetrainingcenterandroid.domain.training.TrainingRepository
import com.example.orangetrainingcenterandroid.domain.training.model.TrainingDomainModel
import com.example.orangetrainingcenterandroid.domain.training.model.TrainingResult
import java.time.LocalDate
import java.util.*
import javax.inject.Inject
class FetchAllAvailableTrainingsUseCase @Inject constructor(private val trainingRepository: TrainingRepository) {
   /* suspend fun fetchAllAvailableTrainings(searchQuery: String, themeQuery: String, cursor: String? = null): TrainingResult {
        val allTrainings = mutableListOf<TrainingDomainModel>()
        var nextCursor: String? = null

        while (true) {
            val result = trainingRepository.fetchAvailableTrainings(searchQuery, themeQuery, nextCursor)
            allTrainings.addAll(result.trainings)

            nextCursor = result.nextCursor
            if (nextCursor == null) {
                break
            }
        }

        return TrainingResult(allTrainings, null)
    }*/

    suspend fun fetchAllAvailableTrainings(searchQuery: String, themeQuery: String, startDateQuery: LocalDate?, endDateQuery: LocalDate?, cursor: String? = null): TrainingResult {
        val allTrainings = mutableListOf<TrainingDomainModel>()
        val  result = trainingRepository.fetchAvailableTrainings(searchQuery, themeQuery, startDateQuery,endDateQuery,cursor)
        allTrainings.addAll(result.trainings)


        return TrainingResult(allTrainings, result.nextCursor)
    }
}