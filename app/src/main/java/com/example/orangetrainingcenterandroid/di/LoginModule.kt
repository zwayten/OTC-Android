package com.example.orangetrainingcenterandroid.di

import android.content.Context
import android.content.SharedPreferences
import com.example.orangetrainingcenterandroid.data.login.remote.api.LoginApi
import com.example.orangetrainingcenterandroid.data.login.remote.api.ResetPasswordApi
import com.example.orangetrainingcenterandroid.data.login.repository.LoginRepositoryImpl
import com.example.orangetrainingcenterandroid.data.login.repository.ResetPasswordRepositoryImpl
import com.example.orangetrainingcenterandroid.domain.login.LoginRepository
import com.example.orangetrainingcenterandroid.domain.login.ResetPasswordRepository
import com.example.orangetrainingcenterandroid.domain.login.usecase.*
import com.example.orangetrainingcenterandroid.presentation.launch_screen.LaunchScreenViewModel
import com.example.orangetrainingcenterandroid.presentation.login.LoginViewModel
import com.example.orangetrainingcenterandroid.presentation.reset_password.ResetPasswordViewModel
import com.example.orangetrainingcenterandroid.presentation.settings.SettingsViewModel
import com.example.orangetrainingcenterandroid.presentation.util.SessionCache
import com.google.firebase.messaging.FirebaseMessaging
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
class LoginModule {

    @Singleton
    @Provides
    fun provideLoginApi(retrofit: Retrofit) : LoginApi {

        return retrofit.create(LoginApi::class.java)
    }

    @Singleton
    @Provides
    fun provideLoginRepository(loginApi: LoginApi,@Named("TOKEN") sharedPreferences: SharedPreferences, @Named("NOTIFICATION") notificationSharedPreferences: SharedPreferences,@Named("USER") userSharedPreferences: SharedPreferences) : LoginRepository {
        return LoginRepositoryImpl(loginApi,sharedPreferences,notificationSharedPreferences,userSharedPreferences)
    }
    @Singleton
    @Provides
    fun provideLoginUseCase(loginRepository: LoginRepository) :LoginUseCase{
        return LoginUseCase(loginRepository)
    }


    @Provides
    fun provideLoginViewModel(loginUseCase: LoginUseCase, checkNotificationPermissionUseCase:CheckNotificationPermissionUseCase, handleNotificationPermissionUseCase: HandleNotificationPermissionUseCase, firebaseMessaging: FirebaseMessaging,sessionCache: SessionCache): LoginViewModel {
        return LoginViewModel(loginUseCase,checkNotificationPermissionUseCase, handleNotificationPermissionUseCase,firebaseMessaging,sessionCache)
    }

    @Provides
    @Named("TOKEN")
    fun provideTokenSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
    }

    @Provides
    @Named("USER")
    fun provideUserSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("USER", Context.MODE_PRIVATE)
    }


    @Provides
    @Named("NOTIFICATION")
    fun provideNotificationSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("NOTIFICATION_FLAG", Context.MODE_PRIVATE)
    }


    @Singleton
    @Provides
    fun provideCheckNotificationPermissionUseCase(loginRepository: LoginRepository) : CheckNotificationPermissionUseCase {
        return CheckNotificationPermissionUseCase(loginRepository)
    }

    @Singleton
    @Provides
    fun provideHandleNotificationPermissionUseCase(loginRepository: LoginRepository) : HandleNotificationPermissionUseCase {
        return HandleNotificationPermissionUseCase(loginRepository)
    }

    @Singleton
    @Provides
    fun provideLaunchScreenViewModel(seesionCache: SessionCache): LaunchScreenViewModel {
        return LaunchScreenViewModel(seesionCache)
    }


    @Singleton
    @Provides
    fun provideResetPasswordApi(retrofit: Retrofit) : ResetPasswordApi {
        return retrofit.create(ResetPasswordApi::class.java)
    }

    @Singleton
    @Provides
    fun provideResetPasswordRepository(resetPasswordApi: ResetPasswordApi) : ResetPasswordRepository {
        return ResetPasswordRepositoryImpl(resetPasswordApi)
    }
    @Singleton
    @Provides
    fun provideResetPasswordViewModel(resetPasswordUseCase: ResetPasswordUseCase): ResetPasswordViewModel {
        return ResetPasswordViewModel(resetPasswordUseCase)
    }

    @Singleton
    @Provides
    fun provideResetPasswordUseCase(resetPasswordRepository: ResetPasswordRepository) : ResetPasswordUseCase {
        return ResetPasswordUseCase(resetPasswordRepository)
    }

    @Singleton
    @Provides
    fun provideLogoutUseCase(loginRepository: LoginRepository) : LogoutUseCase {
        return LogoutUseCase(loginRepository)
    }

    @Singleton
    @Provides
    fun provideSettingsViewModel(logoutUseCase: LogoutUseCase, sessionCache: SessionCache) : SettingsViewModel {
        return SettingsViewModel(logoutUseCase,sessionCache)
    }

    @Singleton
    @Provides
    fun provideCheckTokenValidityUseCase(loginRepository: LoginRepository) : CheckTokenValidityUseCase {
        return CheckTokenValidityUseCase(loginRepository)
    }

}