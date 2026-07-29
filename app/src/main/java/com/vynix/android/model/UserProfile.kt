package com.vynix.android.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String,
    val username: String,
    val email: String,
    val avatarUrl: String? = null,
    val totalQuizzes: Int? = null,
    val highestScore: Int? = null,
    val currentRank: Int? = null
)
