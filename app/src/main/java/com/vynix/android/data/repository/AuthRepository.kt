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

    private var _authApi: AuthApi? = null

    private suspend fun getAuthApi(): Result<AuthApi> {
        _authApi?.let { return Result.success(it) }
        return try {
            val api = ApiClient.authApi
            _authApi = api
            Result.success(api)
        } catch (t: Throwable) {
            Result.failure(
                Exception(
                    "Cannot create API client: ${t.javaClass.simpleName}: ${t.message}",
                    t
                )
            )
        }
    }

    suspend fun requestOtp(email: String): Result<OtpResponse> {
        val apiResult = getAuthApi()
        val api = apiResult.getOrElse { return Result.failure(it) }
        return try {
            val response = withContext(Dispatchers.IO) {
                api.requestOtp(OtpRequest(email))
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
            Result.failure(Exception(t.javaClass.simpleName + ": " + (t.message ?: "Unexpected OTP request error")))
        }
    }

    suspend fun verifyOtp(email: String, code: String): Result<LoginResponse> {
        val apiResult = getAuthApi()
        val api = apiResult.getOrElse { return Result.failure(it) }
        return try {
            val request = VerifyOtpRequest(email, code)
            val response = withContext(Dispatchers.IO) {
                api.verifyOtp(request)
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
            Result.failure(Exception(t.javaClass.simpleName + ": " + (t.message ?: "Unexpected OTP verification error")))
        }
    }

    suspend fun refreshToken(refreshToken: String): Result<LoginResponse> {
        val apiResult = getAuthApi()
        val api = apiResult.getOrElse { return Result.failure(it) }
        val request = RefreshTokenRequest(refreshToken)
        return try {
            val response = withContext(Dispatchers.IO) {
                api.refreshToken(request)
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
            Result.failure(Exception(t.javaClass.simpleName + ": " + (t.message ?: "Token refresh error")))
        }
    }

    suspend fun logout(): Result<Unit> {
        val apiResult = getAuthApi()
        val api = apiResult.getOrElse { return Result.failure(it) }
        return try {
            val response = withContext(Dispatchers.IO) {
                api.logout()
            }
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = safeErrorBody(response.errorBody())
                Result.failure(Exception("Logout failed (${response.code()}): $errorMsg"))
            }
        } catch (t: Throwable) {
            Result.failure(Exception(t.javaClass.simpleName + ": " + (t.message ?: "Logout error")))
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
