package io.lb.lbgameshop.core.util

import androidx.compose.ui.graphics.Color

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
