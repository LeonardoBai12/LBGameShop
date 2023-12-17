package io.lb.lbgameshop.order.presentation.my_orders

import io.lb.lbgameshop.order.domain.model.Order

sealed class MyOrderEvent {
    data class NavigateToOrderDetails(val order: Order) : MyOrderEvent()
}
