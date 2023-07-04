package com.example.orangetrainingcenterandroid.data.mapper


import com.example.orangetrainingcenterandroid.data.poll.remote.dto.PollResponse
import com.example.orangetrainingcenterandroid.domain.poll.model.PollChoice
import com.example.orangetrainingcenterandroid.domain.poll.model.PollDomainModel
import kotlin.math.roundToInt

object PollMapper {



    fun mapToDomain(pollResponse: PollResponse): PollDomainModel {
        val pollModel = pollResponse.poll
        val choiceList = pollModel.choices.map { choice ->
            PollChoice(
                choice = choice.choice,
                votes =  choice.votes.roundToInt()
            )
        }
        return PollDomainModel(
            _id = pollModel._id,
            title = pollModel.title,
            description = pollModel.description,
            choices = choiceList,
            hasVoted = pollModel.hasVoted
        )
    }
}