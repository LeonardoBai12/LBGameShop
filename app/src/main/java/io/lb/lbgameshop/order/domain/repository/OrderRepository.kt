package io.lb.lbgameshop.order.domain.repository

import io.lb.lbgameshop.core.util.Resource
import io.lb.lbgameshop.order.domain.model.Order
import io.lb.lbgameshop.order.domain.model.OrderItem
import io.lb.lbgameshop.sign_in.domain.model.UserData
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getItemsFromOrder(userData: UserData, order: Order) : Flow<Resource<List<OrderItem>>>
    suspend fun addOrderItem(userData: UserData, orderItem: OrderItem)
    suspend fun removeOrderItem(userData: UserData, orderItem: OrderItem)
    suspend fun finishOrder(userData: UserData, order: Order)
    suspend fun resetUnfinishedOrder(userData: UserData, order: Order)
    fun getUnfinishedOrder(userData: UserData): Flow<Resource<Order?>>
    fun getFinishedOrders(userData: UserData): Flow<Resource<List<Order>>>
}
