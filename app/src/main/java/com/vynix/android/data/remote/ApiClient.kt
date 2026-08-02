package com.vynix.android.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.vynix.android.data.remote.api.*
import com.vynix.android.data.remote.interceptor.AuthInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "http://192.168.68.106:3002/"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    // Build OkHttp client inside a safe block so any configuration error is caught
    private val okHttpClient: OkHttpClient by lazy {
        try {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(AuthInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
        } catch (t: Throwable) {
            throw RuntimeException("Failed to create OkHttp client: ${t.message}", t)
        }
    }

    private val retrofit: Retrofit by lazy {
        try {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
        } catch (t: Throwable) {
            throw RuntimeException("Failed to create Retrofit instance: ${t.message}", t)
        }
    }

    val authApi: AuthApi by lazy {
        try {
            retrofit.create(AuthApi::class.java)
        } catch (t: Throwable) {
            throw RuntimeException("Failed to create AuthApi: ${t.message}", t)
        }
    }
    val quizApi: QuizApi by lazy {
        try {
            retrofit.create(QuizApi::class.java)
        } catch (t: Throwable) {
            throw RuntimeException("Failed to create QuizApi: ${t.message}", t)
        }
    }
    val walletApi: WalletApi by lazy {
        try {
            retrofit.create(WalletApi::class.java)
        } catch (t: Throwable) {
            throw RuntimeException("Failed to create WalletApi: ${t.message}", t)
        }
    }
    val leaderboardApi: LeaderboardApi by lazy {
        try {
            retrofit.create(LeaderboardApi::class.java)
        } catch (t: Throwable) {
            throw RuntimeException("Failed to create LeaderboardApi: ${t.message}", t)
        }
    }
    val supportApi: SupportApi by lazy {
        try {
            retrofit.create(SupportApi::class.java)
        } catch (t: Throwable) {
            throw RuntimeException("Failed to create SupportApi: ${t.message}", t)
        }
    }
    val settingsApi: SettingsApi by lazy {
        try {
            retrofit.create(SettingsApi::class.java)
        } catch (t: Throwable) {
            throw RuntimeException("Failed to create SettingsApi: ${t.message}", t)
        }
    }
}
