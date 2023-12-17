package io.lb.lbgameshop.game.data.remote

import io.lb.lbgameshop.game.domain.model.Game
import retrofit2.Response
import retrofit2.http.GET

/**
 * Retrofit related data requests.
 */
interface GameClient {
    @GET("/api/1.0/deals")
    suspend fun getGames(): Response<List<Game>>
}
