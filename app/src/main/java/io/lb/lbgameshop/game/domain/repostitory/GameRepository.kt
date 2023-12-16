package io.lb.lbgameshop.game.domain.repostitory

import io.lb.lbgameshop.core.util.Resource
import io.lb.lbgameshop.game.domain.model.Game
import kotlinx.coroutines.flow.Flow

/**
 * Repository to game related data requests.
 */
interface GameRepository {
    /**
     * Request a list of games.
     */
    suspend fun getGames(): Flow<Resource<List<Game>>>
}
