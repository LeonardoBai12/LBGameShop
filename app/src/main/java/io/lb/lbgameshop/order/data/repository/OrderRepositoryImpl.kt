package io.lb.lbgameshop.order.data.repository

import io.lb.lbgameshop.core.util.Resource
import io.lb.lbgameshop.order.data.remote.RealtimeDatabaseClient
import io.lb.lbgameshop.order.domain.model.Order
import io.lb.lbgameshop.order.domain.model.OrderItem
import io.lb.lbgameshop.order.domain.repository.OrderRepository
import io.lb.lbgameshop.sign_in.domain.model.UserData
import kotlinx.coroutines.flow.Flow

class OrderRepositoryImpl(
    private val client: RealtimeDatabaseClient
) : OrderRepository {
    override fun getItemsFromOrder(
        userData: UserData,
        order: Order
    ): Flow<Resource<List<OrderItem>>> {
        return client.getItemsFromOrder(userData, order)
    }

    override suspend fun addOrderItem(
        userData: UserData,
        order: Order,
        orderItem: OrderItem
    ) {
        client.addOrderItem(userData, order, orderItem)
    }

    override suspend fun removeOrderItem(userData: UserData, orderItem: OrderItem) {
        client.removeOrderItem(userData, orderItem)
    }

    override suspend fun finishOrder(userData: UserData, order: Order) {
        client.finishOrder(userData, order)
    }

    override suspend fun resetUnfinishedOrder(userData: UserData, order: Order) {
        client.resetUnfinishedOrder(userData, order)
    }

    override fun getUnfinishedOrder(userData: UserData): Flow<Resource<Order?>> {
        return client.getUnfinishedOrder(userData)
    }

    override fun getFinishedOrders(userData: UserData): Flow<Resource<List<Order>>> {
        return client.getFinishedOrders(userData)
    }
}
