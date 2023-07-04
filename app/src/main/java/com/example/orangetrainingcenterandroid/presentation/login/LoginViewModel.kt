package com.example.orangetrainingcenterandroid.presentation.login


import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangetrainingcenterandroid.MainActivity
import com.example.orangetrainingcenterandroid.common.ApiException
import com.example.orangetrainingcenterandroid.domain.login.model.LoginResult
import com.example.orangetrainingcenterandroid.domain.login.usecase.CheckNotificationPermissionUseCase
import com.example.orangetrainingcenterandroid.domain.login.usecase.HandleNotificationPermissionUseCase
import com.example.orangetrainingcenterandroid.domain.login.usecase.LoginUseCase
import com.example.orangetrainingcenterandroid.presentation.reset_password.ResetPasswordActivity
import com.example.orangetrainingcenterandroid.presentation.util.Session
import com.example.orangetrainingcenterandroid.presentation.util.SessionCache
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase, private val checkNotificationPermissionUseCase: CheckNotificationPermissionUseCase, private val handleNotificationPermissionUseCase: HandleNotificationPermissionUseCase, private val firebaseMessaging: FirebaseMessaging, private val sessionCache:SessionCache) : ViewModel()
{

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val user: LoginResult) : LoginState()
        data class Error(val message: String) : LoginState()
    }


    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val loginState = mutableStateOf<LoginState>(LoginState.Idle)
    val checkPermissionState = mutableStateOf("")
    val isLoggedIn = mutableStateOf(false)




    init {

        //getFCMToken()
        checkNotificationPermission()
        reset()

    }
    fun reset() {
        email.value = ""
        password.value = ""
        loginState.value = LoginState.Idle
        isLoggedIn.value = false // Reset the flag

    }

     fun checkNotificationPermission() {
        checkPermissionState.value =
            checkNotificationPermissionUseCase.checkNotificationPermission().toString()
    }
    fun handlePermissionRequest(response: Boolean){

        handleNotificationPermissionUseCase.handleNotificationPermission(response)
        checkNotificationPermission()
    }



    fun loginUser() {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                loginState.value = LoginState.Loading
                val user = loginUseCase.login(email.value, password.value)

                // Save session to the cache
                val session = Session(
                    token = user.token.token.toString(),
                    expiresAt = 200,
                    isLoggedIn = true
                )
                sessionCache.saveSession(session)

                loginState.value = LoginState.Success(user)
                isLoggedIn.value = true




            } catch (e: ApiException) {
                Timber.tag(ContentValues.TAG).e(e, "Exception occurred")
                loginState.value = LoginState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun navigateToMain(context: Context) {
        if (isLoggedIn.value) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            (context as Activity).finish()
        }
        }

    fun navigateToResetPassword(context: Context) {
            val intent = Intent(context, ResetPasswordActivity::class.java)
            context.startActivity(intent)
            (context as Activity).finish()
    }

    private fun subscribeToTopic(userId: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(userId)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Topic subscription successful
                    // Handle success or perform any other necessary actions
                    println("Successfully subscribed user $userId to topic")
                } else {
                    // Topic subscription failed
                    // Handle failure or perform any other necessary actions
                    println("Error subscribing user $userId to topic: ${task.exception}")
                }
            }
    }


}