package com.example.orangetrainingcenterandroid.di

import android.content.Context
import android.content.SharedPreferences
import com.example.orangetrainingcenterandroid.presentation.util.SessionCache
import com.example.orangetrainingcenterandroid.presentation.util.SessionCacheImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
class SessionCacheModule {
    @Provides
    @Singleton
    @Named("session_prefs")
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSessionCache(@Named("session_prefs") sharedPreferences: SharedPreferences): SessionCache {
        return SessionCacheImpl(sharedPreferences)
    }
}