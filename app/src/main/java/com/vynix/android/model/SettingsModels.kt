package com.vynix.android.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val notificationsEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val profileVisibility: String = "public",     // "public", "friends", "private"
    val showOnlineStatus: Boolean = true
)

@Serializable
data class VersionInfo(
    val version: String,
    val buildNumber: Int,
    val releaseNotes: String
)
