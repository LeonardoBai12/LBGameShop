package io.lb.lbgameshop.order.data.remote

import com.google.firebase.database.DatabaseReference
import io.lb.lbgameshop.core.util.Resource
import io.lb.lbgameshop.order.domain.model.Order
import io.lb.lbgameshop.sign_in.domain.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class RealtimeDatabaseClientImpl(
    val database: DatabaseReference
) : RealtimeDatabaseClient {
    override suspend fun addOrderItem(userData: UserData, order: Order) {
        database.child(userData.userId ?: return)
            .child(order.uuid)
            .setValue(order)
            .await()
    }

    override suspend fun finishOrder(userData: UserData, order: Order) {
        database.child(userData.userId ?: return)
            .child(order.uuid)
            .setValue(order.copy(isFinished = true))
            .await()
    }

    override suspend fun resetUnfinishedOrder(userData: UserData, order: Order) {
        database.child(userData.userId ?: return)
            .child(order.uuid)
            .setValue(null)
            .await()
    }

    override suspend fun getUnfinishedOrder(userData: UserData) : Flow<Resource<Order?>> = flow {
        emit(Resource.Loading(true))

        val result = database.child(userData.userId ?: return@flow).get().await()
        val orders = result.children.map {
            Order.fromSnapshot(it.value as HashMap<String, String>)
        }

        if (orders.isNotEmpty()) {
            emit(
                Resource.Success(
                    data = orders.first { it.isFinished.not() }
                )
            )
        } else {
            emit(
                Resource.Success(
                    data = null
                )
            )
        }

        emit(Resource.Loading(false))
    }

    @Suppress("UNCHECKED_CAST")
    override fun getFinishedOrders(userData: UserData) = flow {
        emit(Resource.Loading(true))

        val result = database.child(userData.userId ?: return@flow).get().await()
        val orders = result.children.map {
            Order.fromSnapshot(it.value as HashMap<String, String>)
        }

        emit(
            Resource.Success(
                data = orders.sortedWith(
                    compareBy(
                        Order::createdDate,
                    )
                ).filter { it.isFinished }
            )
        )

        emit(Resource.Loading(false))
    }
}
