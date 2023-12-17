package io.lb.lbgameshop.order.domain.model

import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
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
}

fun List<OrderItem>.toJson() = URLEncoder.encode(
    Gson().toJson(this).toString(),
    StandardCharsets.UTF_8.toString()
).replace("+", " ")
