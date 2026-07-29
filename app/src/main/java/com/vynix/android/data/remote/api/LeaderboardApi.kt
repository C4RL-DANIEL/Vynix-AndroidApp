package com.vynix.android.data.remote.api

import com.vynix.android.model.LeaderboardEntry
import retrofit2.Response
import retrofit2.http.GET

interface LeaderboardApi {
    @GET("leaderboard/global")
    suspend fun globalLeaderboard(): Response<List<LeaderboardEntry>>

    @GET("leaderboard/friends")
    suspend fun friendsLeaderboard(): Response<List<LeaderboardEntry>>

    @GET("leaderboard/regional")
    suspend fun regionalLeaderboard(): Response<List<LeaderboardEntry>>
}
