package com.vynix.android.data.repository

import com.vynix.android.data.remote.ApiClient
import com.vynix.android.data.remote.api.AuthApi
import com.vynix.android.model.LoginResponse
import com.vynix.android.model.OtpRequest
import com.vynix.android.model.OtpResponse
import com.vynix.android.model.VerifyOtpRequest
import com.vynix.android.model.RefreshTokenRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException

class AuthRepository {

    private val authApi: AuthApi by lazy { ApiClient.authApi }

    suspend fun requestOtp(email: String): Result<OtpResponse> {
        return try {
            val response = withContext(Dispatchers.IO) {
                authApi.requestOtp(OtpRequest(email))
            }
            if (response.isSuccessful) {
                try {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Empty OTP response"))
                    }
                } catch (se: SerializationException) {
                    Result.failure(Exception("Failed to parse OTP response: ${se.message}"))
                }
            } else {
                val errorMsg = safeErrorBody(response.errorBody())
                Result.failure(Exception("Request email OTP failed (${response.code()}): $errorMsg"))
            }
        } catch (t: Throwable) {
            Result.failure(Exception(t.message ?: "Unexpected OTP request error"))
        }
    }

    suspend fun verifyOtp(email: String, code: String): Result<LoginResponse> {
        return try {
            val request = VerifyOtpRequest(email, code)
            val response = withContext(Dispatchers.IO) {
                authApi.verifyOtp(request)
            }
            if (response.isSuccessful) {
                try {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Empty OTP verification response"))
                    }
                } catch (se: SerializationException) {
                    Result.failure(Exception("Failed to parse OTP verification response: ${se.message}"))
                }
            } else {
                val errorMsg = safeErrorBody(response.errorBody())
                Result.failure(Exception("Verify email OTP failed (${response.code()}): $errorMsg"))
            }
        } catch (t: Throwable) {
            Result.failure(Exception(t.message ?: "Unexpected OTP verification error"))
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
                val errorMsg = safeErrorBody(response.errorBody())
                Result.failure(Exception("Token refresh failed (${response.code()}): $errorMsg"))
            }
        } catch (t: Throwable) {
            Result.failure(Exception(t.message ?: "Unexpected token refresh error"))
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
                val errorMsg = safeErrorBody(response.errorBody())
                Result.failure(Exception("Logout failed (${response.code()}): $errorMsg"))
            }
        } catch (t: Throwable) {
            Result.failure(Exception(t.message ?: "Unexpected logout error"))
        }
    }

    private fun safeErrorBody(errorBody: okhttp3.ResponseBody?): String {
        return try {
            errorBody?.string() ?: "Unknown error"
        } catch (_: Exception) {
            "Unknown error"
        }
    }
}
