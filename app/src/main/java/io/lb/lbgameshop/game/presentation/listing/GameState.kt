package io.lb.lbgameshop.game.presentation.listing

import io.lb.lbgameshop.game.domain.model.Game

data class GameState(
    val games: List<Game> = emptyList(),
    val loading: Boolean = true,
)
