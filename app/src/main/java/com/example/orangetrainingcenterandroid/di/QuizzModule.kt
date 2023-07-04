package com.example.orangetrainingcenterandroid.di

import android.content.SharedPreferences
import com.example.orangetrainingcenterandroid.data.quizz.remote.api.QuizzApi
import com.example.orangetrainingcenterandroid.data.quizz.repository.QuizzRepositoryImpl
import com.example.orangetrainingcenterandroid.domain.quizz.QuizzRepository
import com.example.orangetrainingcenterandroid.domain.quizz.usecase.GetLastAnimationQuizzUseCase
import com.example.orangetrainingcenterandroid.domain.quizz.usecase.GetQuizByTrainingUseCase
import com.example.orangetrainingcenterandroid.domain.quizz.usecase.HasVotedUseCase
import com.example.orangetrainingcenterandroid.domain.quizz.usecase.VoteOnQuizzUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton


@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class QuizzModule {

    @Singleton
    @Provides
    fun provideQuizzApi(retrofit: Retrofit) : QuizzApi {
        return retrofit.create(QuizzApi::class.java)
    }

    @Singleton
    @Provides
    fun provideQuizzRepository(quizzApi: QuizzApi, @Named("TOKEN") sharedPreferences: SharedPreferences) : QuizzRepository {
        return QuizzRepositoryImpl(quizzApi,sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideGetQuizByTrainingUseCase(quizzRepository: QuizzRepository) : GetQuizByTrainingUseCase {
        return GetQuizByTrainingUseCase(quizzRepository)
    }

    @Singleton
    @Provides
    fun provideVoteOnQuizzUseCase(quizzRepository: QuizzRepository) : VoteOnQuizzUseCase {
        return VoteOnQuizzUseCase(quizzRepository)
    }
    @Singleton
    @Provides
    fun provideHasVotedUseCase(quizzRepository: QuizzRepository) : HasVotedUseCase {
        return HasVotedUseCase(quizzRepository)
    }

    @Singleton
    @Provides
    fun provideGetLastAnimationQuizzUseCase(quizzRepository:QuizzRepository): GetLastAnimationQuizzUseCase{
        return GetLastAnimationQuizzUseCase(quizzRepository)
    }
}