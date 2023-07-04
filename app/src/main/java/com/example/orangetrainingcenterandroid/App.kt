package com.example.orangetrainingcenterandroid

import android.app.Application
import com.example.orangetrainingcenterandroid.common.GlobalExceptionHandler
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler(GlobalExceptionHandler(this))
        AndroidThreeTen.init(this)
    }
}