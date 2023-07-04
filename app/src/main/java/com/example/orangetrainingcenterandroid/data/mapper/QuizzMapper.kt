package com.example.orangetrainingcenterandroid.data.mapper



import com.example.orangetrainingcenterandroid.data.model.AnswersApi
import com.example.orangetrainingcenterandroid.data.model.QuestionsApi
import com.example.orangetrainingcenterandroid.data.model.QuizzApiModel
import com.example.orangetrainingcenterandroid.domain.quizz.model.AnswerDomain
import com.example.orangetrainingcenterandroid.domain.quizz.model.QuestionDomain
import com.example.orangetrainingcenterandroid.domain.quizz.model.QuizzDomainModel

object QuizzMapper {
    fun mapToDomain(apiModel: QuizzApiModel): QuizzDomainModel {
        return QuizzDomainModel(
            _id = apiModel._id,
            type = apiModel.type,
            description = apiModel.description,
            questions = apiModel.questions.map { mapQuestionToDomain(it) }
        )
    }

    private fun mapQuestionToDomain(apiQuestion: QuestionsApi): QuestionDomain {
        return QuestionDomain(
            questionNumber = apiQuestion.question_number,
            questionText = apiQuestion.question_text,
            answers = apiQuestion.answers.map { mapAnswerToDomain(it) },
            _id = apiQuestion._id
        )
    }

    private fun mapAnswerToDomain(apiAnswer: AnswersApi): AnswerDomain {
        return AnswerDomain(
            optionText = apiAnswer.option_text,
            isCorrect = apiAnswer.is_correct,
            answersIndex = apiAnswer.answers_index,
            _id = apiAnswer._id
        )
    }
}
