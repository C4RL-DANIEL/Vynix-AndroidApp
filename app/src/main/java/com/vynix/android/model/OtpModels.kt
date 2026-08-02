package com.vynix.android.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OtpRequest(
    @SerialName("email") val email: String
)

@Serializable
data class OtpResponse(
    @SerialName("ok") val ok: Boolean = false,
    @SerialName("message") val message: String = ""
)

@Serializable
data class VerifyOtpRequest(
    @SerialName("email") val email: String,
    @SerialName("code") val code: String
)
