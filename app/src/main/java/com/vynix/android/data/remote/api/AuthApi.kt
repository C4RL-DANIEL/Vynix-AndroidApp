package com.vynix.android.data.remote.api

import com.vynix.android.model.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>

    @GET("auth/profile")
    suspend fun getProfile(): Response<UserProfile>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<LoginResponse>

    @POST("auth/logout")
    suspend fun logout(): Response<Unit>
}
