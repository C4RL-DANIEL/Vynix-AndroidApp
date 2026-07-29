package com.vynix.android.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val refreshToken: String,
    val user: UserProfile
)
