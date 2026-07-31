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
                val body = response.body()
                if (body != null) Result.success(body)
                else Result.failure(Exception("Invalid login response body"))
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("Login failed (${response.code()}): $errorMsg"))
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
                val body = response.body()
                if (body != null) Result.success(body)
                else Result.failure(Exception("Invalid registration response body"))
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("Registration failed (${response.code()}): $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun requestOtp(telegramId: String): Result<Unit> {
        return try {
            val response = withContext(Dispatchers.IO) {
                authApi.requestOtp(OtpRequest(telegramId))
            }
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("Request OTP failed (${response.code()}): $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verifyOtp(telegramId: String, code: String): Result<LoginResponse> {
        return try {
            val request = VerifyOtpRequest(telegramId, code)
            val response = withContext(Dispatchers.IO) {
                authApi.verifyOtp(request)
            }
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) Result.success(body)
                else Result.failure(Exception("Empty OTP verification response"))
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("Verify OTP failed (${response.code()}): $errorMsg"))
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
                val body = response.body()
                if (body != null) Result.success(body)
                else Result.failure(Exception("Empty token refresh response"))
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("Token refresh failed (${response.code()}): $errorMsg"))
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
                val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("Logout failed (${response.code()}): $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
