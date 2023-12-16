package io.lb.lbgameshop.game.domain.use_cases

import io.lb.lbgameshop.game.domain.repostitory.GameRepository

class GetGamesUseCase(
    private val repository: GameRepository
) {
}
