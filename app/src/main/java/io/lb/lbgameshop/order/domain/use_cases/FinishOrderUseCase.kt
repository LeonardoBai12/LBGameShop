package io.lb.lbgameshop.order.domain.use_cases

import io.lb.lbgameshop.order.domain.model.Order
import io.lb.lbgameshop.order.domain.model.OrderItem
import io.lb.lbgameshop.order.domain.repository.OrderRepository
import io.lb.lbgameshop.sign_in.domain.model.UserData

class FinishOrderUseCase(
    val repository: OrderRepository
) {
    suspend operator fun invoke(
        userData: UserData,
        order: Order,
        items: List<OrderItem>
    ) {
        if (items.isEmpty()) {
            throw Exception("You can't finish an order without any items.")
        }

        repository.finishOrder(userData, order)
    }
}
