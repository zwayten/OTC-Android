package com.example.orangetrainingcenterandroid.data.model

import com.google.gson.annotations.SerializedName

data class TokenValidity(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("success")
    var success: Boolean? = true,
)
