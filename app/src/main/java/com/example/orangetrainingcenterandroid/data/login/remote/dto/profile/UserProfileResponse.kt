package com.example.orangetrainingcenterandroid.data.login.remote.dto.profile

import com.example.orangetrainingcenterandroid.data.model.UserApiModel
import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("user")
    val user: UserApiModel,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("success")
    var success: Boolean? = true,

)
