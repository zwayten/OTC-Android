package com.example.orangetrainingcenterandroid


import android.content.ContentValues
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.orangetrainingcenterandroid.common.ApiException
import com.example.orangetrainingcenterandroid.domain.login.usecase.CheckTokenValidityUseCase
import com.example.orangetrainingcenterandroid.presentation.util.SessionCache
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionCache: SessionCache,
    private val checkTokenValidityUseCase: CheckTokenValidityUseCase,
    @Named("USER") private val userSharedPreferences: SharedPreferences
) : ViewModel() {
    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        val activeSession = sessionCache.getActiveSession()
        val isLoggedIn = activeSession?.isLoggedIn ?: false
        setLoggedIn(isLoggedIn)
        subscribeToTopic()
        subscribeToPoll()
        subscribeToAnimationQuizz()

    }
    private fun subscribeToTopic() {
        val userId = userSharedPreferences.getString("USER", null)
        if (userId != null) {
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

    private fun subscribeToPoll() {


            FirebaseMessaging.getInstance().subscribeToTopic("poll")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Topic subscription successful
                        // Handle success or perform any other necessary actions
                        println("Successfully subscribed user to poll topic")
                    } else {
                        // Topic subscription failed
                        // Handle failure or perform any other necessary actions
                        println("Error subscribing user to topic: ${task.exception}")
                    }
                }

    }

    private fun subscribeToAnimationQuizz() {
        FirebaseMessaging.getInstance().subscribeToTopic("animation")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Topic subscription successful
                    // Handle success or perform any other necessary actions
                    println("Successfully subscribed user to poll topic")
                } else {
                    // Topic subscription failed
                    // Handle failure or perform any other necessary actions
                    println("Error subscribing user to topic: ${task.exception}")
                }
            }

    }
    fun checkTokenValidity(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                checkTokenValidityUseCase.checkTokenValidity()
            }catch(e: ApiException) {

                Log.e(ContentValues.TAG, "Exception occurred", e)
            }

        }
    }

    fun setTabIndex(index: Int) {
        _selectedTabIndex.value = index
    }
    fun navigateToSelectedTab(
        navController: NavHostController,
    ) {
        val selectedIndex = when {
            navController.currentDestination?.route == "trainings" -> 1

            else -> selectedTabIndex.value
        }
        val destination = when (selectedIndex) {
            0 -> "home"
            1 ->"trainings"
            2 -> "profile"
            3 -> "settings"
            else -> error("Invalid index")
        }

        navController.navigate(destination)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        _isLoggedIn.value = isLoggedIn
    }

    fun setLoggedIn2(){
         _isLoggedIn.value = sessionCache.getActiveSession()?.isLoggedIn ?: false
    }
}