package com.vynix.android.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vynix.android.data.repository.AuthRepository
import com.vynix.android.security.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val otpCode: String = "",
    val isLoading: Boolean = false,
    val isRequestingOtp: Boolean = false,
    val otpSent: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository()
    private val tokenManager = TokenManager(application)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(
            email = value,
            otpSent = false,
            otpCode = "",
            error = null
        )
    }

    fun onOtpCodeChange(value: String) {
        _uiState.value = _uiState.value.copy(otpCode = value, error = null)
    }

    fun requestOtp() {
        val current = _uiState.value
        if (current.email.isBlank()) {
            _uiState.value = current.copy(error = "Email is required")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isRequestingOtp = true,
                error = null
            )
            try {
                val result = authRepository.requestOtp(current.email)
                result.fold(
                    onSuccess = { otpResponse ->
                        // Safely handle a body that might be null or missing fields
                        if (otpResponse?.ok == true) {
                            _uiState.value = _uiState.value.copy(
                                isRequestingOtp = false,
                                otpSent = true,
                                error = null
                            )
                        } else {
                            _uiState.value = _uiState.value.copy(
                                isRequestingOtp = false,
                                error = otpResponse?.message?.ifBlank { "Unknown error" } ?: "Unknown error"
                            )
                        }
                    },
                    onFailure = { e ->
                        _uiState.value = _uiState.value.copy(
                            isRequestingOtp = false,
                            error = e.message ?: "Failed to request OTP"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isRequestingOtp = false,
                    error = e.message ?: "Unexpected error"
                )
            }
        }
    }

    fun verifyOtp() {
        val current = _uiState.value
        if (current.email.isBlank() || current.otpCode.isBlank()) {
            _uiState.value = current.copy(error = "Email and OTP code required")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val result = authRepository.verifyOtp(current.email, current.otpCode)
                result.fold(
                    onSuccess = { response ->
                        val accessToken = response.token
                        val refreshToken = response.refreshToken
                        if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = "Server returned an empty token"
                            )
                        } else {
                            // Force non‑null since we already checked
                            tokenManager.saveAccessToken(accessToken!!)
                            tokenManager.saveRefreshToken(refreshToken!!)
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                success = true
                            )
                        }
                    },
                    onFailure = { e ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = e.message ?: "OTP verification failed"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "OTP verification error"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
