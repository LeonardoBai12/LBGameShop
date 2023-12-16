package io.lb.lbgameshop.game.presentation.listing

sealed class GameEvent {
    data class SearchedForTask(val filter: String) : GameEvent()
}
