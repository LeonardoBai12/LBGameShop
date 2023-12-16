package io.lb.lbgameshop.core.util

fun Double.toCurrencyString(): String {
    val formattedString = String.format("%.2f", this)
    return "U$ $formattedString".replace(".", ",")
}
