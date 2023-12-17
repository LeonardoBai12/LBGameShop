package io.lb.lbgameshop.order.domain.use_cases

import io.lb.lbgameshop.core.util.Resource
import io.lb.lbgameshop.order.domain.model.Order
import io.lb.lbgameshop.order.domain.model.OrderItem
import io.lb.lbgameshop.order.domain.repository.OrderRepository
import io.lb.lbgameshop.sign_in.domain.model.UserData
import kotlinx.coroutines.flow.Flow

class GetItemsFromOrderUseCase(
    val repository: OrderRepository
) {
    fun invoke(userData: UserData, order: Order): Flow<Resource<List<OrderItem>>> {
        return repository.getItemsFromOrder(userData, order)
    }
}
