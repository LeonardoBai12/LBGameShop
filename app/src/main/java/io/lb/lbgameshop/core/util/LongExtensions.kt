package io.lb.lbgameshop.core.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long.toReleaseDate(): String? {
    val localDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli((this.times(1000L))),
        ZoneId.systemDefault()
    )
    val formatter = DateTimeFormatter.ofPattern("MMM/yyyy")
    return localDateTime.format(formatter)
}
