package com.example.orangetrainingcenterandroid.presentation.profile

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangetrainingcenterandroid.common.ApiException
import com.example.orangetrainingcenterandroid.data.login.remote.dto.LoginRequest
import com.example.orangetrainingcenterandroid.data.login.remote.dto.ProfileUpdateRequest.ProfileUpdateRequest
import com.example.orangetrainingcenterandroid.data.login.remote.dto.password_update_request.PasswordUpdateRequest
import com.example.orangetrainingcenterandroid.data.login.remote.dto.password_update_request.PasswordUpdateResponse
import com.example.orangetrainingcenterandroid.data.login.remote.dto.profile.UserProfileResponse
import com.example.orangetrainingcenterandroid.data.login.remote.dto.sendRequest.SendRequest
import com.example.orangetrainingcenterandroid.domain.login.UserProfileRepository
import com.example.orangetrainingcenterandroid.domain.login.model.UserDomainModel
import com.example.orangetrainingcenterandroid.domain.login.model.UserProfileResult
import com.example.orangetrainingcenterandroid.domain.login.usecase.*
import com.example.orangetrainingcenterandroid.presentation.login.LoginViewModel
import com.example.orangetrainingcenterandroid.presentation.util.SessionCache
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProfileUseCase: UserProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val sendRequestUseCase: SendRequestUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val sessionCache: SessionCache
) : ViewModel() {


    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _direction = MutableStateFlow("")
    val direction: StateFlow<String> = _direction

    private val _gender = MutableStateFlow("")
    val gender: StateFlow<String> = _gender

    private val _role = MutableStateFlow("")
    val role: StateFlow<String> = _role

    private val _id = MutableStateFlow("")
    val id: StateFlow<String> = _id


    private val _departement = MutableStateFlow("")
    val departement: StateFlow<String> = _departement


    val errorMessage = mutableStateOf("")

    var isPasswordChangeSuccess = mutableStateOf(false)

    private val _userProfile = MutableStateFlow<UserProfileResult?>(null)
    val userProfile: StateFlow<UserProfileResult?> = _userProfile

    val sendRequestResponse = mutableStateOf("")


    fun resetErrorMessage() {
        errorMessage.value = ""
    }

    fun updatePassword(password: String, newPassword: String): PasswordUpdateResponse? {
        var passwordUpdateResponse: PasswordUpdateResponse? = null
        viewModelScope.launch(Dispatchers.IO) {
            val passwordUpdateRequest = PasswordUpdateRequest(password, newPassword)
            withContext(Dispatchers.IO) {
                try {
                    passwordUpdateResponse =
                        updatePasswordUseCase.updatePassword(passwordUpdateRequest)

                    isPasswordChangeSuccess.value = true

                } catch (e: ApiException) {
                    isPasswordChangeSuccess.value = false
                    errorMessage.value = e.message ?: "Unknown error occurred"
                    Log.e(TAG, "Exception occurred", e)
                }
            }
        }
        return passwordUpdateResponse
    }


    // Load the user profile data
    fun fetchProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val profile = userProfileUseCase.fetchProfile()
                _userProfile.value = profile
            } catch (e: ApiException) {
                val errorResult = e.message
                userProfile.value?.message = errorResult
            }
        }
    }

    fun loadProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchProfile() // Fetch the user profile data
            userProfile.value?.let { profile ->
                _fullName.value = profile.user.fullName
                _email.value = profile.user.email
                _phoneNumber.value = profile.user.phoneNumber
                //photo.value = profile.user.photo
                _direction.value = profile.user.direction
                _gender.value = profile.user.gender
                _role.value = profile.user.role
                _departement.value = profile.user.departement
                _id.value = profile.user._id
            }
        }
    }


    fun updateProfile(fullName: String, phoneNumber: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val updateRequest = ProfileUpdateRequest(fullName, phoneNumber)

            withContext(Dispatchers.IO) {
                try {
                    updateProfileUseCase.updateProfile(updateRequest)
                } catch (e: ApiException) {
                    // handle error
                    Timber.tag(TAG).e(e, "Exception occurred")
                }
            }
        }
    }


    fun sendRequest(description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val sendRequest = SendRequest(description)
                sendRequestUseCase.sendReqeust(sendRequest)
            } catch (e: ApiException) {
                Timber.tag(ContentValues.TAG).e(e, "Exception occurred")
            }
        }
    }


    fun logout() {
        logoutUseCase.logout()
        sessionCache.clearSession()
    }

    fun subscribeToTopic(userId: String) {
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

    fun unsubscribeFromTopic(topic: String) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Unsubscription successful
                } else {
                    // Unsubscription failed
                }
            }
    }
}




