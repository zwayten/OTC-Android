package com.example.orangetrainingcenterandroid.presentation.util

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import javax.inject.Inject
import javax.inject.Named

class SessionCacheImpl @Inject constructor(@Named("session_prefs") private val sharedPreferences: SharedPreferences): SessionCache{
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val adapter = moshi.adapter(Session::class.java)

    override fun saveSession(session: Session) {
        sharedPreferences.edit()
            .putString("session", adapter.toJson(session))
            .apply()
    }

    override fun getActiveSession(): Session? {
        val json = sharedPreferences.getString("session", null) ?: return null
        return adapter.fromJson(json)
    }

    override fun clearSession() {
        val existingSessionJson = sharedPreferences.getString("session", null)
        existingSessionJson?.let {
            val existingSession = adapter.fromJson(it)
            val updatedSession = existingSession?.copy(isLoggedIn = false)
            val updatedSessionJson = adapter.toJson(updatedSession)
            sharedPreferences.edit().putString("session", updatedSessionJson).apply()
        }
    }
}