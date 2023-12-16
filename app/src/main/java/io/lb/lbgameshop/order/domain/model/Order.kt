package io.lb.lbgameshop.order.domain.model

import com.google.gson.Gson
import io.lb.lbgameshop.core.util.toOrderItemList
import java.util.Calendar
import java.util.UUID

data class Order(
    val uuid: String = UUID.randomUUID().toString(),
    val orderId: Int = UUID.fromString(uuid).variant(),
    val items: List<OrderItem> = emptyList(),
    val isFinished: Boolean = false,
    val createdDate: Long = Calendar.getInstance().timeInMillis
) {
    companion object {
        fun fromJson(json: String): Order = Gson().fromJson(json, Order::class.java)

        fun fromSnapshot(hashMap: HashMap<String, String>): Order {
            return Order(
                uuid = hashMap["uuid"] ?: "",
                orderId = hashMap["orderId"]?.toInt() ?: 0,
                items = hashMap["items"]?.toOrderItemList() ?: emptyList(),
                isFinished = hashMap["isFinished"].toBoolean(),
                createdDate = hashMap["createdDate"]?.toLong() ?: 0L
            )
        }
    }

    fun toJson() = Gson().toJson(this).toString()
}
