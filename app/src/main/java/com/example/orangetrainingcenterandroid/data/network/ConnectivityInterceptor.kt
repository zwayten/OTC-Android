package com.example.orangetrainingcenterandroid.data.network

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.widget.Toast
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

class ConnectivityInterceptor(private val context: Context) : Interceptor {
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        try {
            val response = chain.proceed(request)
            if (!response.isSuccessful) {
                println("Response unsuccessful")
                showToast("Response unsuccessful")
            }
            return response
        } catch (e: IOException) {
            println("Connection lost")
            showToast("Connection lost!")
            throw e
        }
    }

    private fun showToast(message: String) {
        mainHandler.post {
            val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP, 0, 0)
            toast.show()
        }
    }
}







