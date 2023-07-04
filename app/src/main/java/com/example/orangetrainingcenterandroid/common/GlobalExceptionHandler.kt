package com.example.orangetrainingcenterandroid.common

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.widget.Toast
import java.net.SocketTimeoutException

class GlobalExceptionHandler(private val context: Context) : Thread.UncaughtExceptionHandler {
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        if (throwable is SocketTimeoutException) {

            showConnectionLostToast()
        }
    }

    private fun showConnectionLostToast() {
        mainHandler.post {
            val toast = Toast.makeText(context, "Connection lost!", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP, 0, 0)
            toast.show()
        }
    }
}