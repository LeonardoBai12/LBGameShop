package io.lb.lbgameshop.core.util

import io.lb.lbgameshop.game.domain.model.Game

fun List<Game>.filterBy(filter: String) =
    filter {
        filter.lowercase() in it.title.lowercase()
    }
