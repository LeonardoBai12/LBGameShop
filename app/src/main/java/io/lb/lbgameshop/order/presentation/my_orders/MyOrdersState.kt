package io.lb.lbgameshop.order.presentation.my_orders

import io.lb.lbgameshop.order.domain.model.Order
import io.lb.lbgameshop.order.domain.model.OrderItem

data class MyOrdersState(
    val orders: List<Order> = emptyList(),
    val loading: Boolean = true
)
