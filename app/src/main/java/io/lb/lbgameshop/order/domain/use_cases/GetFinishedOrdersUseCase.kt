package io.lb.lbgameshop.order.domain.use_cases

import io.lb.lbgameshop.core.util.Resource
import io.lb.lbgameshop.order.domain.model.Order
import io.lb.lbgameshop.order.domain.repository.OrderRepository
import io.lb.lbgameshop.sign_in.domain.model.UserData
import kotlinx.coroutines.flow.Flow

class GetFinishedOrdersUseCase(
    val repository: OrderRepository
) {
    fun invoke(userData: UserData): Flow<Resource<Order?>> {
        return repository.getUnfinishedOrder(userData)
    }
}
