package com.vynix.android.data.remote.api

import com.vynix.android.model.*
import retrofit2.Response
import retrofit2.http.*

interface QuizApi {
    @GET("quiz/categories")
    suspend fun getCategories(): Response<List<QuizCategory>>

    @POST("quiz/start")
    suspend fun startQuiz(@Body request: QuizSessionStartRequest): Response<QuizSessionStartResponse>

    @GET("quiz/{sessionId}/next")
    suspend fun getNextQuestion(@Path("sessionId") sessionId: String): Response<QuizQuestion>

    @POST("quiz/{sessionId}/answer")
    suspend fun submitAnswer(
        @Path("sessionId") sessionId: String,
        @Body request: QuizAnswerRequest
    ): Response<QuizAnswerResponse>

    @GET("quiz/{sessionId}/result")
    suspend fun getQuizResult(@Path("sessionId") sessionId: String): Response<QuizResult>

    @GET("quiz/history")
    suspend fun getQuizHistory(): Response<List<QuizResult>>
}
