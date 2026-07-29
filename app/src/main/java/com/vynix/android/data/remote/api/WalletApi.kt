package com.vynix.android.data.remote.api

import com.vynix.android.model.*
import retrofit2.Response
import retrofit2.http.*

interface WalletApi {
    @GET("wallet/balance")
    suspend fun getBalance(): Response<WalletBalance>

    @GET("wallet/transactions")
    suspend fun getTransactions(): Response<List<TransactionItem>>

    @POST("wallet/withdraw")
    suspend fun withdraw(@Body request: WithdrawalRequest): Response<WithdrawalStatusResponse>

    @GET("wallet/withdrawal-status/{id}")
    suspend fun withdrawalStatus(@Path("id") withdrawalId: String): Response<WithdrawalStatusResponse>
}
