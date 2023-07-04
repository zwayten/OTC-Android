package com.example.orangetrainingcenterandroid.di



import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.orangetrainingcenterandroid.common.BASE_URL
import com.example.orangetrainingcenterandroid.data.network.ConnectivityInterceptor
import com.example.orangetrainingcenterandroid.presentation.login.LoginActivity
import com.example.orangetrainingcenterandroid.presentation.profile.ProfileViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {



    @Singleton
    @Provides
    fun provideRetrofit(okHttp: OkHttpClient) : Retrofit {

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttp)
            .build()
        return retrofit
    }

    @Singleton
    @Provides
    fun provideOkHttp(@ApplicationContext context: Context): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .addInterceptor(ConnectivityInterceptor(context))
            .addInterceptor { chain ->
                val request = chain.request().newBuilder().build()
                chain.proceed(request)
            }.addInterceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)

                // Check the response status code
                if (response.code == 419) {
                    // Redirect to the login screen
                    // Logout the user
                    logout(context)

                    // Redirect to the login screen
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                    return@addInterceptor response
                }

                response
            }
        return builder.build()
    }

    private fun logout(context:Context) {
        val sharedPreferences = context.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("TOKEN", "")
            putBoolean("isLoggedIn", false)
            apply()
        }
    }

}