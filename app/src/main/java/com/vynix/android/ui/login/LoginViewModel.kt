package com.vynix.android.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vynix.android.data.repository.AuthRepository
import com.vynix.android.security.TokenManager
import kotlinx.coroutines.CoroutineExceptionHandler
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

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        // Catch any unhandled exception that would otherwise crash the app,
        // set the UI into a safe state and show the error message.
        _uiState.value = _uiState.value.copy(
            isRequestingOtp = false,
            isLoading = false,
            error = throwable.message ?: "Unexpected error"
        )
    }

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
        viewModelScope.launch(exceptionHandler) {
            _uiState.value = _uiState.value.copy(
                isRequestingOtp = true,
                error = null
            )
            try {
                val result = authRepository.requestOtp(current.email)
                result.fold(
                    onSuccess = {
                        // API responded successfully – mark OTP as sent.
                        _uiState.value = _uiState.value.copy(
                            isRequestingOtp = false,
                            otpSent = true,
                            error = null
                        )
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
        viewModelScope.launch(exceptionHandler) {
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
                            tokenManager.saveAccessToken(accessToken)
                            tokenManager.saveRefreshToken(refreshToken)
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
