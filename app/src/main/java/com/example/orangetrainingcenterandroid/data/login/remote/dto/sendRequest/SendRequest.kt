package com.example.orangetrainingcenterandroid.data.login.remote.dto.sendRequest

import com.google.gson.annotations.SerializedName

data class SendRequest(
    @SerializedName("description") val description: String,
)
data class SendRequestResponse(

    @SerializedName("message")
    var message: String? = null,
    @SerializedName("success")
    var success: Boolean? = true,
)

