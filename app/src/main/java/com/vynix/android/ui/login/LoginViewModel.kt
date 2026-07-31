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
    val telegramId: String = "",
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

    fun onTelegramIdChange(value: String) {
        _uiState.value = _uiState.value.copy(
            telegramId = value,
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
        if (current.telegramId.isBlank()) {
            _uiState.value = current.copy(error = "Telegram ID is required")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isRequestingOtp = true,
                error = null
            )
            val result = authRepository.requestOtp(current.telegramId)
            result.fold(
                onSuccess = {
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
        }
    }

    fun verifyOtp() {
        val current = _uiState.value
        if (current.telegramId.isBlank() || current.otpCode.isBlank()) {
            _uiState.value = current.copy(error = "Telegram ID and OTP code required")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = authRepository.verifyOtp(current.telegramId, current.otpCode)
            result.fold(
                onSuccess = { response ->
                    tokenManager.saveAccessToken(response.token)
                    tokenManager.saveRefreshToken(response.refreshToken)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        success = true
                    )
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "OTP verification failed"
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
