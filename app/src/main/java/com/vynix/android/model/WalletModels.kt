package com.vynix.android.model

import kotlinx.serialization.Serializable

@Serializable
data class WalletBalance(
    val balance: Int
)

@Serializable
data class TransactionItem(
    val id: String,
    val type: String,       // "earned", "spent", "withdrawn"
    val amount: Int,
    val description: String,
    val timestamp: String
)

@Serializable
data class WithdrawalRequest(
    val amount: Int,
    val method: String = "bank" // default placeholder
)

@Serializable
data class WithdrawalStatusResponse(
    val id: String,
    val status: String,      // "pending", "completed", "failed"
    val amount: Int,
    val fee: Int
)
