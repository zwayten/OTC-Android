package com.example.orangetrainingcenterandroid.presentation.home

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangetrainingcenterandroid.common.ApiException
import com.example.orangetrainingcenterandroid.data.poll.remote.dto.VoteRequest
import com.example.orangetrainingcenterandroid.domain.poll.model.PollDomainModel
import com.example.orangetrainingcenterandroid.domain.poll.usecase.GetPollUseCase
import com.example.orangetrainingcenterandroid.domain.poll.usecase.VotePollUseCase
import com.example.orangetrainingcenterandroid.domain.quizz.model.QuizzDomainModel
import com.example.orangetrainingcenterandroid.domain.quizz.usecase.GetLastAnimationQuizzUseCase
import com.example.orangetrainingcenterandroid.domain.quizz.usecase.HasVotedUseCase
import com.example.orangetrainingcenterandroid.domain.training.model.PresenceStateDomainModel
import com.example.orangetrainingcenterandroid.domain.training.model.TrainingDetailsDomainModel
import com.example.orangetrainingcenterandroid.domain.training.model.TrainingDomainModel
import com.example.orangetrainingcenterandroid.domain.training.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPollUseCase: GetPollUseCase,
    private val votePollUseCase: VotePollUseCase,
    private val fetchAllAvailableTrainingsUseCase: FetchAllAvailableTrainingsUseCase,
    private val getLastAnimationQuizzUseCase: GetLastAnimationQuizzUseCase,
    private val getCurrentTrainingUseCase: GetCurrentTrainingUseCase,
    private val hasVotedUseCase: HasVotedUseCase,
    private val confirmAttendanceUseCase: ConfirmAttendanceUseCase,
    private val getPresenceStateUseCase: GetPresenceStateUseCase,
    private val getTrainingsHomeSectionUseCase: GetTrainingsHomeSectionUseCase
): ViewModel() {

    val poll = mutableStateOf<PollDomainModel?>(null)
    val animationQuizz = mutableStateOf<QuizzDomainModel?>(null)
    val currentTraining = mutableStateOf<TrainingDetailsDomainModel?>(null)

    val presenceState = mutableStateOf<PresenceStateDomainModel?>(null)

    private val _hasVoted = MutableStateFlow(false)
    val hasVoted: StateFlow<Boolean> = _hasVoted

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _trainingsStateThisWeek = MutableStateFlow<List<TrainingDomainModel>>(emptyList())
    val trainingsStateThisWeek: StateFlow<List<TrainingDomainModel>> = _trainingsStateThisWeek

    private val _trainingsStateUpComing = MutableStateFlow<List<TrainingDomainModel>>(emptyList())
    val trainingsStateUpComing: StateFlow<List<TrainingDomainModel>> = _trainingsStateUpComing

    private val _trainingsStateTargeted = MutableStateFlow<List<TrainingDomainModel>>(emptyList())
    val trainingsStateTargeted: StateFlow<List<TrainingDomainModel>> = _trainingsStateTargeted

    fun loadHomeScreen() {
        if (_loading.value) {
            return  // If already loading, skip the function call
        }
          viewModelScope.launch(Dispatchers.IO) {
              println("recomposition issue ")
             try {
                 _loading.value = true
                  val pollRes = getPollUseCase.getPoll()
                  poll.value = pollRes

                 val animationRes = getLastAnimationQuizzUseCase.getLastAnimationQuizz()
                 animationQuizz.value = animationRes

                 val currentTrainingRes = getCurrentTrainingUseCase.getCurrentTraining()
                 println("curent training: ${currentTrainingRes}")
                 currentTraining.value = currentTrainingRes



                 val hasVotedRes = currentTraining.value?.let { it.quizzEvaluation?.let { it1 ->
                     hasVotedUseCase.hasVote(
                         it1
                     )
                 } }

                 if (hasVotedRes != null) {
                     _hasVoted.value = hasVotedRes
                 }

                 presenceState.value = currentTraining.value?.let {
                     getPresenceStateUseCase.getPresenceState(
                         it._id)
                 }

                 val trainingSectionsRes = getTrainingsHomeSectionUseCase.getTrainingsHomeSection()
                 _trainingsStateThisWeek.value = trainingSectionsRes.thisWeekTrainings
                 _trainingsStateUpComing.value = trainingSectionsRes.upcomingTrainings
                 _trainingsStateTargeted.value = trainingSectionsRes.targetedTrainings


              } catch (e: ApiException) {
                 Timber.tag(ContentValues.TAG).e(e, "Exception occurred")
             }finally {
                 _loading.value = false
             }
          }

      }

        fun getPreseneceState(id:String){
            viewModelScope.launch(Dispatchers.IO) {
                try {

                    presenceState.value = getPresenceStateUseCase.getPresenceState(id)

        } catch (e: ApiException) {
                    Timber.tag(ContentValues.TAG).e(e, "Exception occurred")
                }
}
        }



    fun votePoll(idPoll: String,index: Int) {
        val voteRequest = VoteRequest(index)
        viewModelScope.launch(Dispatchers.IO) {
            val response = votePollUseCase.votePoll(voteRequest, idPoll)
            if (response != null) {
                if (response.success == true) {
                    poll.value = getPollUseCase.getPoll()
                } else {
                    Timber.tag(TAG).d("problem")
                }
            }
        }
    }


    fun confirmPresence(id:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                confirmAttendanceUseCase.confirmAttendance(id)
            } catch (e: ApiException) {
                Timber.tag(ContentValues.TAG).e(e, "Exception occurred")
            }
        }
    }

    fun changeDateFormat(date: Date, newFormat: String): String {
        val desiredFormat = SimpleDateFormat(newFormat, Locale.ENGLISH)
        desiredFormat.timeZone = TimeZone.getTimeZone("UTC")
        return desiredFormat.format(date)
    }

    fun calculateRemainingTime(endDate: Date): String {
        val currentTime = LocalDateTime.now()
        val endDateTime = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

        val remainingHours = ChronoUnit.HOURS.between(currentTime, endDateTime)
        val remainingDays = remainingHours / 24
        val remainingHoursInDay = remainingHours % 24

        val remainingTime = when {
            remainingHours <= 0L -> "Expired"
            remainingDays == 0L -> "$remainingHoursInDay hours"
            remainingHoursInDay == 0L -> "$remainingDays day(s)"
            else -> "$remainingDays day(s) and $remainingHoursInDay hours"
        }

        return remainingTime
    }



}
