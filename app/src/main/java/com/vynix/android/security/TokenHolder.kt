package com.vynix.android.security

object TokenHolder {
    var tokenProvider: (() -> String?)? = null
    var refreshTokenProvider: (() -> String?)? = null
    var onTokenRefresh: ((String) -> Unit)? = null
}
