package io.lb.lbgameshop.game.domain.use_cases

import io.lb.lbgameshop.core.util.Resource
import io.lb.lbgameshop.game.domain.model.Game
import io.lb.lbgameshop.game.domain.repostitory.GameRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get a list of games on sale.
 */
class GetGamesUseCase(
    private val repository: GameRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<Game>>> {
        return repository.getGames()
    }
}
