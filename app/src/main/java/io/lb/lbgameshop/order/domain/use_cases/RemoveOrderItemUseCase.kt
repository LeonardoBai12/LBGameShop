package io.lb.lbgameshop.order.domain.use_cases

import io.lb.lbgameshop.order.domain.model.OrderItem
import io.lb.lbgameshop.order.domain.repository.OrderRepository
import io.lb.lbgameshop.sign_in.domain.model.UserData

class RemoveOrderItemUseCase(
    val repository: OrderRepository
) {
    suspend operator fun invoke(userData: UserData, orderItem: OrderItem) {
        repository.removeOrderItem(userData, orderItem)
    }
}
