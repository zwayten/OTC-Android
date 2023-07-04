package com.example.orangetrainingcenterandroid.presentation.reset_password

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangetrainingcenterandroid.common.ApiException
import com.example.orangetrainingcenterandroid.data.login.remote.dto.reset_password.ResetPasswordResponse
import com.example.orangetrainingcenterandroid.domain.login.usecase.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(private val resetPasswordUseCase: ResetPasswordUseCase) : ViewModel(){

    val email = mutableStateOf("")

    var isResetPasswordButtonEnabled = mutableStateOf(true)
    var resetPasswordButtonTimer = mutableStateOf(0)
    var emailError = mutableStateOf("")


    private val coroutineScope = CoroutineScope(Dispatchers.IO + viewModelScope.coroutineContext)


    fun resetPassword() {
        val requestBody = mapOf("email" to email.value)
        var resetPasswordResponse = ResetPasswordResponse("");
        coroutineScope.launch {
            try {
                isResetPasswordButtonEnabled.value = false
                 resetPasswordResponse = resetPasswordUseCase.resetPassword(requestBody)

                resetPasswordButtonTimer.value = 20
                repeat(20) {
                    delay(1000) // wait for 1 second
                    resetPasswordButtonTimer.value -= 1

                }
                isResetPasswordButtonEnabled.value = true
            } catch (e: ApiException) {
                isResetPasswordButtonEnabled.value = true
                emailError.value = "Error: ${e.message}"
            }
        }
    }
}