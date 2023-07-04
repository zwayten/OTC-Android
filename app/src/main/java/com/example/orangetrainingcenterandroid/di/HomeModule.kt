package com.example.orangetrainingcenterandroid.di


import android.content.Context
import android.content.SharedPreferences
import com.example.orangetrainingcenterandroid.MainViewModel
import com.example.orangetrainingcenterandroid.data.poll.remote.api.PollApi
import com.example.orangetrainingcenterandroid.data.poll.repository.PollRepositoryImpl
import com.example.orangetrainingcenterandroid.domain.login.usecase.CheckTokenValidityUseCase
import com.example.orangetrainingcenterandroid.domain.poll.PollRepository
import com.example.orangetrainingcenterandroid.domain.poll.usecase.GetPollUseCase
import com.example.orangetrainingcenterandroid.domain.poll.usecase.VotePollUseCase
import com.example.orangetrainingcenterandroid.domain.quizz.usecase.GetLastAnimationQuizzUseCase
import com.example.orangetrainingcenterandroid.domain.quizz.usecase.HasVotedUseCase
import com.example.orangetrainingcenterandroid.domain.training.TrainingRepository
import com.example.orangetrainingcenterandroid.domain.training.usecase.*
import com.example.orangetrainingcenterandroid.presentation.home.HomeViewModel
import com.example.orangetrainingcenterandroid.presentation.util.SessionCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named

import javax.inject.Singleton


@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class HomeModule {


    @Singleton
    @Provides
    fun providePollApi(retrofit: Retrofit) : PollApi {
        return retrofit.create(PollApi::class.java)
    }
    @Singleton
    @Provides
    fun providePollRepository(pollApi: PollApi, @Named("TOKEN") sharedPreferences: SharedPreferences) : PollRepository {
        return PollRepositoryImpl(pollApi,sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideGetPollUseCase(pollRepository: PollRepository) : GetPollUseCase {
        return GetPollUseCase(pollRepository)
    }

    @Singleton
    @Provides
    fun provideVotePollUseCase(pollRepository: PollRepository) : VotePollUseCase {
        return VotePollUseCase(pollRepository)
    }

    @Singleton
    @Provides
    fun provideConfirmAttendanceUseCase(trainingRepository: TrainingRepository): ConfirmAttendanceUseCase {
        return ConfirmAttendanceUseCase(trainingRepository)
    }

    @Singleton
    @Provides
    fun provideGetPresenceStateUseCase(trainingRepository: TrainingRepository): GetPresenceStateUseCase {
        return GetPresenceStateUseCase(trainingRepository)
    }
    @Singleton
    @Provides
    fun provideGetTrainingsHomeSectionUseCase(trainingRepository: TrainingRepository): GetTrainingsHomeSectionUseCase {
        return GetTrainingsHomeSectionUseCase(trainingRepository)
    }


    @Singleton
    @Provides
    fun provideHomeViewModel(getPollUseCase:GetPollUseCase, votePollUseCase:VotePollUseCase,  fetchAllAvailableTrainingsUseCase: FetchAllAvailableTrainingsUseCase,getLastAnimationQuizzUseCase: GetLastAnimationQuizzUseCase,getCurrentTrainingUseCase: GetCurrentTrainingUseCase,hasVotedUseCase: HasVotedUseCase,confirmAttendanceUseCase:ConfirmAttendanceUseCase,getPresenceStateUseCase:GetPresenceStateUseCase,getTrainingsHomeSectionUseCase:GetTrainingsHomeSectionUseCase): HomeViewModel{
        return HomeViewModel(getPollUseCase,votePollUseCase,fetchAllAvailableTrainingsUseCase,getLastAnimationQuizzUseCase,getCurrentTrainingUseCase,hasVotedUseCase,confirmAttendanceUseCase,getPresenceStateUseCase,getTrainingsHomeSectionUseCase)
    }

    @Singleton
    @Provides
    fun provideMainViewModel(sessionCache: SessionCache, checkTokenValidityUseCase: CheckTokenValidityUseCase,@Named("USER") userSharedPreferences: SharedPreferences): MainViewModel {
        return MainViewModel(sessionCache,checkTokenValidityUseCase,userSharedPreferences)
    }


}