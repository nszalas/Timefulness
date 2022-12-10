package com.nszalas.timefulness.extensions

import java.time.LocalDateTime
import java.time.ZoneOffset

fun LocalDateTime.asTimestamp(): Long =
    this.toEpochSecond(ZoneOffset.UTC)

fun Long.asLocalDateTime(): LocalDateTime =
    LocalDateTime.ofEpochSecond(this, 0, ZoneOffset.UTC)
