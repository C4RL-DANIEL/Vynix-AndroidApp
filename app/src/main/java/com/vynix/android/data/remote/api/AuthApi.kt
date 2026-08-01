package com.vynix.android.data.remote.api

import com.vynix.android.model.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {
    @POST("api/app/auth/request-otp")
    suspend fun requestOtp(@Body request: OtpRequest): Response<OtpResponse>

    @POST("api/app/auth/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<LoginResponse>

    @GET("auth/profile")
    suspend fun getProfile(): Response<UserProfile>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<LoginResponse>

    @POST("auth/logout")
    suspend fun logout(): Response<Unit>
}
