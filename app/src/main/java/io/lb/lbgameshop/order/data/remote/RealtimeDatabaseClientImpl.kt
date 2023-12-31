package io.lb.lbgameshop.order.data.remote

import com.google.firebase.database.DatabaseReference
import io.lb.lbgameshop.core.util.Resource
import io.lb.lbgameshop.order.domain.model.Order
import io.lb.lbgameshop.order.domain.model.OrderItem
import io.lb.lbgameshop.sign_in.domain.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class RealtimeDatabaseClientImpl(
    private val database: DatabaseReference
) : RealtimeDatabaseClient {
    override fun getItemsFromOrder(
        userData: UserData,
        order: Order
    ) = flow {
        emit(Resource.Loading(true))

        val result = database.child(userData.userId ?: return@flow)
            .child(order.uuid)
            .child("orderItems")
            .get().await()
        val items = result.children.map {
            OrderItem.fromSnapshot(it.value as HashMap<String, String>)
        }

        emit(
            Resource.Success(
                data = items.sortedWith(
                    compareBy(
                        OrderItem::createdDate,
                    )
                )
            )
        )

        emit(Resource.Loading(false))
    }

    override suspend fun addOrderItem(
        userData: UserData,
        order: Order,
        orderItem: OrderItem
    ) {
        database.child(userData.userId ?: return)
            .child(order.uuid)
            .updateChildren(
                mapOf(
                    "uuid" to order.uuid,
                    "isFinished" to order.isFinished.toString(),
                    "createdDate" to order.createdDate,
                )
            )
            .await()

        database.child(userData.userId)
            .child(order.uuid)
            .child("orderItems")
            .child(orderItem.uuid)
            .setValue(orderItem)
            .await()
    }

    override suspend fun removeOrderItem(userData: UserData, orderItem: OrderItem) {
        database.child(userData.userId ?: return)
            .child(orderItem.orderId)
            .child("orderItems")
            .child(orderItem.uuid)
            .setValue(null)
            .await()
    }

    override suspend fun finishOrder(userData: UserData, order: Order) {
        database.child(userData.userId ?: return)
            .child(order.uuid)
            .updateChildren(
                mapOf(
                    "isFinished" to "true",
                    "finishedDate" to Calendar.getInstance().timeInMillis.toString(),
                )
            )
            .await()
    }

    override suspend fun resetUnfinishedOrder(userData: UserData, order: Order) {
        database.child(userData.userId ?: return)
            .child(order.uuid)
            .setValue(null)
            .await()
    }

    override fun getUnfinishedOrder(userData: UserData): Flow<Resource<Order?>> = flow {
        emit(Resource.Loading(true))

        val result = database.child(userData.userId ?: return@flow).get().await()
        val orders = result.children.map {
            Order.fromSnapshot(it.value as HashMap<String, String>)
        }.filter { it.isFinished.not() }

        if (orders.isNotEmpty()) {
            emit(
                Resource.Success(
                    data = orders.first()
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
                    .reversed()
            )
        )

        emit(Resource.Loading(false))
    }
}
