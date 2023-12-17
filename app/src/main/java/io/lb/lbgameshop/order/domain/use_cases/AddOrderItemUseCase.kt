package io.lb.lbgameshop.order.domain.use_cases

import io.lb.lbgameshop.game.domain.model.Game
import io.lb.lbgameshop.order.domain.model.Order
import io.lb.lbgameshop.order.domain.model.OrderItem
import io.lb.lbgameshop.order.domain.repository.OrderRepository
import io.lb.lbgameshop.sign_in.domain.model.UserData

class AddOrderItemUseCase(
    val repository: OrderRepository
) {
    suspend operator fun invoke(
        userData: UserData,
        order: Order,
        game: Game
    ) {
        val orderItem = OrderItem(
            orderId = order.uuid,
            data = game
        )

        repository.addOrderItem(userData, order, orderItem)
    }
}
