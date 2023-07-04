package com.example.orangetrainingcenterandroid.presentation.quizzevaluation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangetrainingcenterandroid.data.quizz.remote.dto.quizz.VoteRequest
import com.example.orangetrainingcenterandroid.domain.quizz.model.QuizzDomainModel
import com.example.orangetrainingcenterandroid.domain.quizz.model.VoteDomainModel
import com.example.orangetrainingcenterandroid.domain.quizz.usecase.GetQuizByTrainingUseCase
import com.example.orangetrainingcenterandroid.domain.quizz.usecase.VoteOnQuizzUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class QuizzEvaluationViewModel @Inject constructor(private val getQuizByTrainingUseCase: GetQuizByTrainingUseCase, private val voteOnQuizzUseCase: VoteOnQuizzUseCase): ViewModel() {

    val quizz = mutableStateOf<QuizzDomainModel?>(null)
    val score = mutableStateOf<VoteDomainModel?>(null)

    fun displayQuiz(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = getQuizByTrainingUseCase.getQuizByTraining(id)
            println("le quizz ${response}")
            quizz.value = response

        }
    }

    fun voteOnQuizz(id:String, voteRequest: VoteRequest){
        viewModelScope.launch(Dispatchers.IO) {
            val response = voteOnQuizzUseCase.voteOnQuizz(id,voteRequest)
        score.value = response
        }
    }
}