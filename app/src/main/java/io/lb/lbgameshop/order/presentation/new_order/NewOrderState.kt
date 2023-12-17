package io.lb.lbgameshop.order.presentation.new_order

import io.lb.lbgameshop.order.domain.model.Order
import io.lb.lbgameshop.order.domain.model.OrderItem

data class NewOrderState(
    val order: Order = Order(),
    val items: List<OrderItem> = emptyList(),
    val loading: Boolean = true
)
