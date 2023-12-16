package io.lb.lbgameshop.order.domain.model

import io.lb.lbgameshop.game.domain.model.Game
import java.util.UUID

data class OrderItem(
    val uuid: UUID = UUID.randomUUID(),
    val orderItemId: Int = uuid.variant(),
    val data: Game
)
