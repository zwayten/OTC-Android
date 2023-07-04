package com.example.orangetrainingcenterandroid.presentation.launch_screen

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.orangetrainingcenterandroid.presentation.util.SessionCache
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LaunchScreenViewModel @Inject constructor(private val sessionCache:SessionCache) : ViewModel() {

    fun isUserLoggedIn(): Boolean {
        val activeSession = sessionCache.getActiveSession()
        return activeSession?.isLoggedIn ?: false
    }
}