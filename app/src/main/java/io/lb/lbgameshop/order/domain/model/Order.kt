package io.lb.lbgameshop.order.domain.model

import java.util.UUID

data class Order(
    val uuid: UUID = UUID.randomUUID(),
    val orderId: Int = uuid.variant(),
    val items: List<OrderItem>,
    val isFinished: Boolean
)
