package com.example.orangetrainingcenterandroid.presentation.util

interface SessionCache {

    fun saveSession(session: Session)

    fun getActiveSession(): Session?

    fun clearSession()
}