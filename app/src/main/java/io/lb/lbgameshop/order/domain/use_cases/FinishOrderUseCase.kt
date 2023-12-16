package io.lb.lbgameshop.order.domain.use_cases

import io.lb.lbgameshop.order.domain.repository.OrderRepository

class FinishOrderUseCase(
    val repository: OrderRepository
) {
    operator fun invoke() {

    }
}
