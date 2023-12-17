package io.lb.lbgameshop.order.domain.model

import com.google.gson.Gson
import io.lb.lbgameshop.game.domain.model.Game
import java.util.Calendar
import java.util.UUID

data class OrderItem(
    val uuid: String = UUID.randomUUID().toString(),
    val orderId: String,
    val createdDate: String = Calendar.getInstance().timeInMillis.toString(),
    val data: String
) {
    companion object {
        fun fromJson(json: String): OrderItem = Gson().fromJson(json, OrderItem::class.java)

        fun fromSnapshot(hashMap: HashMap<String, String>): OrderItem {
            return OrderItem(
                uuid = hashMap["uuid"] ?: "",
                orderId = hashMap["orderId"] ?: "",
                createdDate = hashMap["createdDate"] ?: "",
                data = hashMap["data"] ?: ""
            )
        }
    }

    fun toJson() = Gson().toJson(this).toString()
}
