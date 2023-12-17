package io.lb.lbgameshop.game.presentation.listing

sealed class GameEvent {
    data class SearchedForGame(val filter: String) : GameEvent()
}
