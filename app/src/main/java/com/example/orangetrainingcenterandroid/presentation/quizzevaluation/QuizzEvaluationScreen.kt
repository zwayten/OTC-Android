package com.example.orangetrainingcenterandroid.presentation.quizzevaluation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.orangetrainingcenterandroid.data.quizz.remote.dto.quizz.AnswersRequest
import com.example.orangetrainingcenterandroid.data.quizz.remote.dto.quizz.VoteRequest
import com.example.orangetrainingcenterandroid.domain.quizz.model.QuestionDomain

data class Question(
    val text: String,
    val options: List<String>,
    val selectedOptions: List<Int>,
    val textAnswer: String
)

/*
val questions = mutableListOf(
        Question(
            text = "Question 1",
            options = listOf("Option 1", "Option 2", "Option 3"),
            selectedOptions = emptyList(),
            textAnswer = ""
        ),
        Question(
            text = "Question 2",
            options = listOf("Option 1", "Option 2", "Option 3"),
            selectedOptions = emptyList(),
            textAnswer = ""
        )
    )
 */

@Composable
fun QuizzScreen(quizzEvaluationViewModel: QuizzEvaluationViewModel, quizzId: String,navController: NavController) {

    quizzEvaluationViewModel.displayQuiz(quizzId)
    val quizDescription = quizzEvaluationViewModel.quizz.value?.description.toString()
    var questions = mutableListOf<QuestionDomain>()
    questions = quizzEvaluationViewModel.quizz.value?.questions?.toMutableList() ?: mutableListOf<QuestionDomain>()

    val selectedAnswersMap = remember { mutableStateMapOf<String, List<Int>>() }

    Column(modifier = Modifier.fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        // Description
        Text(
            text = quizDescription,
            modifier = Modifier.padding(16.dp)
        )

        // Questions
        Column(modifier = Modifier.padding(16.dp)) {
            questions.forEachIndexed { index, question ->
                QuestionItem(
                    question = question,
                    index = index,
                    onSelectedAnswersChanged = { questionId, selectedAnswers ->
                        selectedAnswersMap[questionId] = selectedAnswers
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Display selected answers
        Button(
            onClick = {
                val voteRequest = VoteRequest(
                    answers = selectedAnswersMap.map { (questionId, selectedAnswers) ->
                        AnswersRequest(
                            questionId = questionId,
                            selectedOptions = selectedAnswers
                        )
                    }
                )
                quizzEvaluationViewModel.voteOnQuizz(quizzId,voteRequest)
                navController.popBackStack()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 10.dp)
        ) {
            Text(text = "Submit Answers")
        }
    }
}

@Composable
fun QuestionItem(question: QuestionDomain, index: Int, onSelectedAnswersChanged: (String, List<Int>) -> Unit) {
    val selectedAnswers = remember { mutableStateListOf<Int>() }

    Column {
        // Question Text
        Text(text = "${question.questionNumber}. ${question.questionText}")

        // Options
        question.answers.forEach { answer ->
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selectedAnswers.contains(answer.answersIndex),
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            selectedAnswers.add(answer.answersIndex)
                        } else {
                            selectedAnswers.remove(answer.answersIndex)
                        }
                        onSelectedAnswersChanged(question._id, selectedAnswers)
                    }
                )
                Text(text = answer.optionText)
            }
        }
    }
}