package io.lb.lbgameshop.game.data.remote

import io.lb.lbgameshop.game.domain.model.Game

/**
 * Response of a games getting request.
 */
data class GetGamesResponse(
    val games: List<Game>
)
