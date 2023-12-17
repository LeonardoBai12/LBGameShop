package io.lb.lbgameshop.order.domain.model

import com.google.gson.Gson
import io.lb.lbgameshop.core.util.toOrderItemList
import java.util.Calendar
import java.util.UUID

data class Order(
    val uuid: String = UUID.randomUUID().toString(),
    val isFinished: Boolean = false,
    val createdDate: String = Calendar.getInstance().timeInMillis.toString(),
    val finishedDate: String? = null
) {
    companion object {
        fun fromJson(json: String): Order = Gson().fromJson(json, Order::class.java)

        fun fromSnapshot(hashMap: HashMap<String, String>): Order {
            return Order(
                uuid = hashMap["uuid"].orEmpty(),
                isFinished = hashMap["isFinished"].toBoolean(),
                createdDate = hashMap["createdDate"].orEmpty(),
                finishedDate = hashMap["finishedDate"].orEmpty()
            )
        }
    }

    fun toJson() = Gson().toJson(this).toString()
}
