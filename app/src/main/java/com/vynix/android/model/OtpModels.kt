package com.vynix.android.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OtpRequest(
    @SerialName("telegram_id") val telegramId: String
)

@Serializable
data class VerifyOtpRequest(
    @SerialName("telegram_id") val telegramId: String,
    val code: String
)
