package com.vynix.android.model

import kotlinx.serialization.Serializable

@Serializable
data class OtpRequest(
    val email: String
)

@Serializable
data class VerifyOtpRequest(
    val email: String,
    val code: String
)
