package com.vynix.android.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TokenManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "vynix_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    var token: String?
        get() = prefs.getString("auth_token", null)
        set(value) = prefs.edit()
            .putString("auth_token", value)
            .apply()

    private var storedRefreshToken: String?
        get() = prefs.getString("refresh_token", null)
        set(value) = prefs.edit()
            .putString("refresh_token", value)
            .apply()

    // Compatibility functions used by LoginViewModel
    fun saveAccessToken(value: String) {
        token = value
    }

    fun saveRefreshToken(value: String) {
        storedRefreshToken = value
    }

    fun getAccessToken(): String? {
        return token
    }


    fun clear() {
        prefs.edit()
            .clear()
            .apply()
    }
}
