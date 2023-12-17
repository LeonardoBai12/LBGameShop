package io.lb.lbgameshop.order.domain.model

import com.google.gson.Gson
import io.lb.lbgameshop.game.domain.model.Game
import java.util.Calendar
import java.util.UUID

data class OrderItem(
    val uuid: String = UUID.randomUUID().toString(),
    val orderItemId: Int = UUID.fromString(uuid).variant(),
    val orderId: String,
    val createdDate: Long = Calendar.getInstance().timeInMillis,
    val data: Game
) {
    companion object {
        fun fromJson(json: String): OrderItem = Gson().fromJson(json, OrderItem::class.java)

        fun fromSnapshot(hashMap: HashMap<String, String>): OrderItem {
            return OrderItem(
                uuid = hashMap["uuid"] ?: "",
                orderItemId = hashMap["orderItemId"]?.toInt() ?: 0,
                orderId = hashMap["orderId"] ?: "",
                createdDate = hashMap["createdDate"]?.toLong() ?: 0L,
                data = Game.fromJson(hashMap["data"] ?: "")
            )
        }
    }

    fun toJson() = Gson().toJson(this).toString()
}
