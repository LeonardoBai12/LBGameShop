package io.lb.lbgameshop.game.data.remote

import retrofit2.Response
import retrofit2.http.GET

/**
 * Retrofit related data requests.
 */
interface GameClient {
    @GET("deals")
    suspend fun getGames(): Response<GetGamesResponse>
}
