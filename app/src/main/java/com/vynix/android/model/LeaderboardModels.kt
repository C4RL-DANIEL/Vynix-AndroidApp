package com.vynix.android.model

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardEntry(
    val rank: Int,
    val userId: String,
    val username: String,
    val coinTotal: Int,
    val tier: String = "Bronze"
)
