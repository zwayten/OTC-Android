package com.example.orangetrainingcenterandroid.data.login.remote.dto.ProfileUpdateRequest

import com.google.gson.annotations.SerializedName

data class ProfileUpdateRequest(
    @SerializedName("fullName") var fullName: String,
    @SerializedName("phoneNumber") val phoneNumber: String,


)
