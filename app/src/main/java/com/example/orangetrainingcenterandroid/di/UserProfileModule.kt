package com.example.orangetrainingcenterandroid.di

import android.content.SharedPreferences
import com.example.orangetrainingcenterandroid.data.login.remote.api.LoginApi
import com.example.orangetrainingcenterandroid.data.login.remote.api.UserProfileApi
import com.example.orangetrainingcenterandroid.data.login.repository.LoginRepositoryImpl
import com.example.orangetrainingcenterandroid.data.login.repository.UserProfileRepositoryImpl
import com.example.orangetrainingcenterandroid.domain.login.LoginRepository
import com.example.orangetrainingcenterandroid.domain.login.UserProfileRepository
import com.example.orangetrainingcenterandroid.domain.login.usecase.*
import com.example.orangetrainingcenterandroid.presentation.login.LoginViewModel
import com.example.orangetrainingcenterandroid.presentation.profile.ProfileViewModel
import com.example.orangetrainingcenterandroid.presentation.util.SessionCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class UserProfileModule {

    @Singleton
    @Provides
    fun provideUserProfileApi(retrofit: Retrofit) : UserProfileApi {
        return retrofit.create(UserProfileApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserProfileRepository(userProfileApi: UserProfileApi,@Named("TOKEN") sharedPreferences: SharedPreferences) : UserProfileRepository {
        return UserProfileRepositoryImpl(userProfileApi,sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideUserProfileUseCase(userProfileRepository: UserProfileRepository): UserProfileUseCase {
        return UserProfileUseCase(userProfileRepository)
    }
    @Singleton
    @Provides
    fun provideUpdateProfileUseCase(userProfileRepository: UserProfileRepository,@Named("TOKEN") sharedPreferences: SharedPreferences): UpdateProfileUseCase {
        return UpdateProfileUseCase(userProfileRepository,sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideSendRequestUseCaseUseCase(userProfileRepository: UserProfileRepository): SendRequestUseCase {
        return SendRequestUseCase(userProfileRepository)
    }

    @Singleton
    @Provides
    fun provideUpdatePasswordUseCase(userProfileRepository: UserProfileRepository,@Named("TOKEN") sharedPreferences: SharedPreferences): UpdatePasswordUseCase {
        return UpdatePasswordUseCase(userProfileRepository,sharedPreferences)
    }
    @Provides
    fun provideProfileViewModel(userProfileUseCase: UserProfileUseCase, updateProfileUseCase: UpdateProfileUseCase, updatePasswordUseCase:UpdatePasswordUseCase, sendRequestUseCase:SendRequestUseCase, logoutUseCase: LogoutUseCase, sessionCache: SessionCache): ProfileViewModel {
        return ProfileViewModel(userProfileUseCase,updateProfileUseCase,updatePasswordUseCase,sendRequestUseCase,logoutUseCase,sessionCache)
    }

}