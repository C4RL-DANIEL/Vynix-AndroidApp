package com.vynix.android.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OtpRequest(
    @SerialName("email") val email: String
)

@Serializable
data class VerifyOtpRequest(
    @SerialName("email") val email: String,
    val code: String
)
