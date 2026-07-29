package com.vynix.android.model

import kotlinx.serialization.Serializable

@Serializable
data class SupportTicket(
    val id: String,
    val subject: String,
    val status: String,      // "open", "closed", "waiting"
    val createdAt: String
)

@Serializable
data class SupportMessage(
    val id: String,
    val senderId: String,
    val message: String,
    val timestamp: String
)
