package io.lb.lbgameshop.order.domain.use_cases

import io.lb.lbgameshop.order.domain.repository.OrderRepository

class AddOrderItem(
    val repository: OrderRepository
) {
    operator fun invoke() {

    }
}
