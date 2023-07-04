package com.example.orangetrainingcenterandroid.presentation.training_details


import android.content.ContentValues
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangetrainingcenterandroid.common.ApiException
import com.example.orangetrainingcenterandroid.domain.quizz.usecase.HasVotedUseCase
import com.example.orangetrainingcenterandroid.domain.training.model.TrainerProfileDomainModel
import com.example.orangetrainingcenterandroid.domain.training.model.TrainingDetailsDomainModel
import com.example.orangetrainingcenterandroid.domain.training.usecase.GetTrainerProfileUseCase
import com.example.orangetrainingcenterandroid.domain.training.usecase.RequestParticipationUseCase
import com.example.orangetrainingcenterandroid.domain.training.usecase.TrainingDetailsUseCase
import com.example.orangetrainingcenterandroid.domain.training.usecase.VerifyParticipationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import javax.inject.Inject



@HiltViewModel
class TrainingDetailsViewModel @Inject constructor(
    private val trainingDetailsUseCase: TrainingDetailsUseCase,
    private val verifyParticipationUseCase: VerifyParticipationUseCase,
    private val requestParticipationUseCase: RequestParticipationUseCase,
    private val getTrainerProfileUseCase: GetTrainerProfileUseCase,
    private val hasVotedUseCase: HasVotedUseCase
) : ViewModel() {
    val trainingState = mutableStateOf<TrainingDetailsDomainModel?>(null)
    val trainerProfile = mutableStateOf<TrainerProfileDomainModel?>(null)
    val errorState = mutableStateOf<String?>(null)
    val loadingState = mutableStateOf(false)
    val remainingTimeState = mutableStateOf("")
    private val _isParticipating = mutableStateOf(false)
    val isParticipating: State<Boolean> = _isParticipating

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading



    val isAccepted = mutableStateOf(false)
    val isPending = mutableStateOf(false)
    val button_text_paticipation = mutableStateOf("")

    private val _hasVoted = MutableStateFlow(false)
    val hasVoted: StateFlow<Boolean> = _hasVoted


    fun verifyHasVoted(id:String){
        viewModelScope.launch(Dispatchers.IO) {
            _hasVoted.value = hasVotedUseCase.hasVote(id)
        }
    }

     fun verifyParticipation(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = verifyParticipationUseCase.verifyParticipation(id)


            if (response?.accepted == true && !response.pending) {
                isAccepted.value = true
                isPending.value = false
                button_text_paticipation.value = "Leave Training"
            }
            if (response?.accepted == false && response.pending) {
                isAccepted.value = false
                isPending.value = true
                button_text_paticipation.value = "Cancel Participation"
            }
            if (response?.accepted == false && !response.pending) {
                isAccepted.value = false
                isPending.value = false
                button_text_paticipation.value = "Participate"
            }
        }
    }

    fun participate() {
        if (_isLoading.value) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true


                val participationState = trainingState.value?.let {
                    requestParticipationUseCase.requestParticipation(it._id)
                }

                // Update the participation status based on the result
                verifyParticipation(trainingState.value?._id ?: "")

            } catch (e: Exception) {
                // Handle the error case
                // Handle any necessary error handling logic
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchTrainerProfile(id:String){
        viewModelScope.launch(Dispatchers.IO) {
            loadingState.value = true
            try {
                val profile = getTrainerProfileUseCase.getTrainerProfile(id)
                trainerProfile.value = profile
            } catch (e: ApiException) {
                errorState.value = "Failed to fetch trainer profile"
                Timber.tag(ContentValues.TAG).e(e, "Exception occurred")
            } finally {
                loadingState.value = false
            }
        }
    }

    fun fetchTrainingDetail(id: String) {

        viewModelScope.launch(Dispatchers.IO) {
            loadingState.value = true
            try {
                val fetchedTraining = trainingDetailsUseCase.trainingDetails(id)
                trainingState.value = fetchedTraining
                if (fetchedTraining != null) {
                    calculateRemainingTime(fetchedTraining.startDate)
                }

            } catch (e: ApiException) {
                errorState.value = "Failed to fetch training details"
                println("problem here: $e")
            } finally {
                loadingState.value = false
            }
        }
    }


    private fun calculateRemainingTime(startDate: Date) {
        val today = LocalDate.now()
        val startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        val remainingDays = ChronoUnit.DAYS.between(today, startLocalDate)
        val remainingMonths = ChronoUnit.MONTHS.between(today, startLocalDate)
        val remainingDaysInMonth = remainingDays - remainingMonths * 30

        val remainingTime = when {
            remainingDays <= 0L -> "On-Going"
            remainingDays == 1L -> "1 Day remaining"
            remainingDays < 30L -> "$remainingDays Days remaining"
            remainingMonths == 1L && remainingDaysInMonth == 0L -> "1 Month remaining"
            remainingMonths == 1L -> "1 month and $remainingDaysInMonth Days remaining"
            remainingMonths > 1L && remainingDaysInMonth == 0L -> "$remainingMonths Months remaining"
            else -> "$remainingMonths Months and $remainingDaysInMonth Days remaining"
        }

        remainingTimeState.value = remainingTime
    }


    fun changeDateFormat(date: Date, newFormat: String): String {
        val desiredFormat = SimpleDateFormat(newFormat, Locale.ENGLISH)
        desiredFormat.timeZone = TimeZone.getTimeZone("UTC")
        return desiredFormat.format(date)
    }
}

