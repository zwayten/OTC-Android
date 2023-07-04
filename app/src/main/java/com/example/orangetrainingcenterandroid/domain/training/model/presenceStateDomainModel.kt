package com.example.orangetrainingcenterandroid.domain.training.model

data class PresenceDomainModel(
    val dayNumber: Int,
    val attended: Boolean,
    val isCurrentDay: Boolean
)

data class PresenceStateDomainModel(
    val presenceState: List<PresenceDomainModel>
)
