package com.vynix.android.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("token") val token: String? = null,
    @SerialName("refreshToken") val refreshToken: String? = null,
    @SerialName("user") val user: UserProfile? = null
)
