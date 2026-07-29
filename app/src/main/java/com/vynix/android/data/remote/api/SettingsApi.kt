package com.vynix.android.data.remote.api

import com.vynix.android.model.*
import retrofit2.Response
import retrofit2.http.*

interface SettingsApi {
    @GET("settings/preferences")
    suspend fun getPreferences(): Response<UserPreferences>

    @PATCH("settings/preferences")
    suspend fun updatePreferences(@Body preferences: UserPreferences): Response<UserPreferences>

    @GET("settings/version-info")
    suspend fun getVersionInfo(): Response<VersionInfo>
}
