package io.lb.lbgameshop.order.data.remote

import io.lb.lbgameshop.core.util.Resource
import io.lb.lbgameshop.order.domain.model.Order
import io.lb.lbgameshop.sign_in.domain.model.UserData
import kotlinx.coroutines.flow.Flow

interface RealtimeDatabaseClient {
    suspend fun addOrderItem(userData: UserData, order: Order)
    suspend fun finishOrder(userData: UserData, order: Order)
    suspend fun resetUnfinishedOrder(userData: UserData, order: Order)
    fun getUnfinishedOrder(userData: UserData) : Flow<Resource<Order?>>
    fun getFinishedOrders(userData: UserData) : Flow<Resource<List<Order>>>
}
