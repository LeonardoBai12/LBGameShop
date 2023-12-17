package io.lb.lbgameshop.order.presentation.new_order

import io.lb.lbgameshop.order.domain.model.OrderItem

sealed class NewOrderEvent {
    data class RequestDelete(val orderItem: OrderItem) : NewOrderEvent()
    object RestoreTask : NewOrderEvent()
}
