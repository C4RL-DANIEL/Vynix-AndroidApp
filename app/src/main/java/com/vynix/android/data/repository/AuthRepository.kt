package com.vynix.android.data.repository

import com.vynix.android.data.remote.ApiClient
import com.vynix.android.data.remote.api.AuthApi
import com.vynix.android.model.LoginRequest
import com.vynix.android.model.RegisterRequest
import com.vynix.android.model.LoginResponse
import com.vynix.android.model.OtpRequest
import com.vynix.android.model.VerifyOtpRequest
import com.vynix.android.model.RefreshTokenRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {

    private val authApi: AuthApi = ApiClient.authApi

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        // Kept for backward compatibility but not used now
        return try {
            val request = LoginRequest(email, password)
            val response = withContext(Dispatchers.IO) {
                authApi.login(request)
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Login failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String, name: String): Result<LoginResponse> {
        val request = RegisterRequest(email, password, name)
        return try {
            val response = withContext(Dispatchers.IO) {
                authApi.register(request)
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Registration failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun requestOtp(email: String): Result<Unit> {
        return try {
            val response = withContext(Dispatchers.IO) {
                authApi.requestOtp(OtpRequest(email))
            }
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Request OTP failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verifyOtp(email: String, code: String): Result<LoginResponse> {
        return try {
            val request = VerifyOtpRequest(email, code)
            val response = withContext(Dispatchers.IO) {
                authApi.verifyOtp(request)
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Verify OTP failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun refreshToken(refreshToken: String): Result<LoginResponse> {
        val request = RefreshTokenRequest(refreshToken)
        return try {
            val response = withContext(Dispatchers.IO) {
                authApi.refreshToken(request)
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Token refresh failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Unit> {
        return try {
            val response = withContext(Dispatchers.IO) {
                authApi.logout()
            }
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Logout failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
