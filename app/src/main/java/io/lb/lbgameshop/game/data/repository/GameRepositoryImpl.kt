package io.lb.lbgameshop.game.data.repository

import io.lb.lbgameshop.game.data.remote.GameClient
import io.lb.lbgameshop.game.domain.repostitory.GameRepository

class GameRepositoryImpl(
    private val client: GameClient
) : GameRepository {

}
