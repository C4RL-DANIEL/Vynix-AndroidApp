package com.vynix.android.data.remote.api

import com.vynix.android.model.*
import retrofit2.Response
import retrofit2.http.*

interface SupportApi {
    @GET("support/tickets")
    suspend fun getTickets(): Response<List<SupportTicket>>

    @POST("support/tickets")
    suspend fun createTicket(@Body request: Map<String, String>): Response<SupportTicket>

    @GET("support/tickets/{id}/messages")
    suspend fun getMessages(@Path("id") ticketId: String): Response<List<SupportMessage>>

    @POST("support/tickets/{id}/messages")
    suspend fun sendReply(
        @Path("id") ticketId: String,
        @Body message: Map<String, String>
    ): Response<SupportMessage>
}
