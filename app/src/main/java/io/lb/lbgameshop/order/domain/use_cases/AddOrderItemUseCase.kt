package io.lb.lbgameshop.order.domain.use_cases

import io.lb.lbgameshop.order.domain.model.Order
import io.lb.lbgameshop.order.domain.repository.OrderRepository
import io.lb.lbgameshop.sign_in.domain.model.UserData

class AddOrderItemUseCase(
    val repository: OrderRepository
) {
    suspend operator fun invoke(userData: UserData, order: Order) {
        repository.addOrderItem(userData, order)
    }
}
