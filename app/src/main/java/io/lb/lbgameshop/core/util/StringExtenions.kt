package io.lb.lbgameshop.core.util

import androidx.compose.ui.graphics.Color
import com.google.gson.Gson
import io.lb.lbgameshop.order.domain.model.OrderItem

fun String.isValidEmail(): Boolean {
    val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$")
    return isNotBlank() && matches(emailRegex)
}

fun String.reviewColor(): Color {
    return if ("positive" in this.lowercase()) {
        Color.Green
    } else if ("negative" in this.lowercase()) {
        Color.Red
    } else {
        Color.Yellow
    }
}

fun String.toOrderItemList() : List<OrderItem> {
    return Gson().fromJson(this, Array<OrderItem>::class.java).asList()
}
