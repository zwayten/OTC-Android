package com.example.orangetrainingcenterandroid.di


import android.content.SharedPreferences
import com.example.orangetrainingcenterandroid.data.training.remote.api.TrainingApi
import com.example.orangetrainingcenterandroid.data.training.repository.TrainingRepositoryImpl
import com.example.orangetrainingcenterandroid.domain.quizz.usecase.GetQuizByTrainingUseCase
import com.example.orangetrainingcenterandroid.domain.quizz.usecase.HasVotedUseCase
import com.example.orangetrainingcenterandroid.domain.quizz.usecase.VoteOnQuizzUseCase
import com.example.orangetrainingcenterandroid.domain.training.TrainingRepository
import com.example.orangetrainingcenterandroid.domain.training.usecase.*
import com.example.orangetrainingcenterandroid.presentation.quizzevaluation.QuizzEvaluationViewModel
import com.example.orangetrainingcenterandroid.presentation.training.FetchAllAvailableTrainingsViewModel
import com.example.orangetrainingcenterandroid.presentation.training_details.TrainingDetailsViewModel

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class TrainingModule {
    @Singleton
    @Provides
    fun provideTrainingApi(retrofit: Retrofit) : TrainingApi {
        return retrofit.create(TrainingApi::class.java)
    }
    @Singleton
    @Provides
    fun provideTrainingRepository(trainingApi: TrainingApi, @Named("TOKEN") sharedPreferences: SharedPreferences) : TrainingRepository {
        return TrainingRepositoryImpl(trainingApi,sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideFetchAllAvailableTrainingsUseCase(trainingRepository: TrainingRepository) : FetchAllAvailableTrainingsUseCase {
        return FetchAllAvailableTrainingsUseCase(trainingRepository)
    }

    @Singleton
    @Provides
    fun provideGetAllThemesUseCase(trainingRepository: TrainingRepository) : GetAllThemesUseCase {
        return GetAllThemesUseCase(trainingRepository)
    }


    @Singleton
    @Provides
    fun provideFetchAllAvailableTrainingsViewModel(fetchAllAvailableTrainingsUseCase: FetchAllAvailableTrainingsUseCase, getAllThemesUseCase:GetAllThemesUseCase): FetchAllAvailableTrainingsViewModel {
        return FetchAllAvailableTrainingsViewModel(fetchAllAvailableTrainingsUseCase,getAllThemesUseCase)
    }

    @Singleton
    @Provides
    fun provideTrainingDetailsUseCase(trainingRepository: TrainingRepository) : TrainingDetailsUseCase {
        return TrainingDetailsUseCase(trainingRepository)
    }
    @Singleton
    @Provides
    fun provideRequestParticipationUseCase(trainingRepository: TrainingRepository,@Named("TOKEN") sharedPreferences: SharedPreferences) : RequestParticipationUseCase {
        return RequestParticipationUseCase(trainingRepository,sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideVerifyParticipationUseCase(trainingRepository: TrainingRepository,@Named("TOKEN") sharedPreferences: SharedPreferences) : VerifyParticipationUseCase {
        return VerifyParticipationUseCase(trainingRepository,sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideGetTrainerProfileUseCase(trainingRepository: TrainingRepository): GetTrainerProfileUseCase{
        return GetTrainerProfileUseCase(trainingRepository)
    }

    @Singleton
    @Provides
    fun provideGetCurrentTrainingUseCase(trainingRepository: TrainingRepository): GetCurrentTrainingUseCase{
        return GetCurrentTrainingUseCase(trainingRepository)
    }


    @Singleton
    @Provides
    fun provideTrainingDetailsViewModel(trainingDetailsUseCase: TrainingDetailsUseCase,verifyParticipationUseCase:VerifyParticipationUseCase ,requestParticipationUseCase:RequestParticipationUseCase,getTrainerProfileUseCase:GetTrainerProfileUseCase,hasVotedUseCase: HasVotedUseCase): TrainingDetailsViewModel {
        return TrainingDetailsViewModel(trainingDetailsUseCase,verifyParticipationUseCase ,requestParticipationUseCase,getTrainerProfileUseCase,hasVotedUseCase)
    }

    @Singleton
    @Provides
    fun provideQuizzEvaluationViewModel(getQuizByTrainingUseCase: GetQuizByTrainingUseCase,voteOnQuizzUseCase: VoteOnQuizzUseCase): QuizzEvaluationViewModel {
        return QuizzEvaluationViewModel(getQuizByTrainingUseCase, voteOnQuizzUseCase)
    }


}