package com.nszalas.timefulness.extensions

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

fun LocalDateTime.asTimestamp(): Long =
    this.toEpochSecond(ZoneOffset.UTC)

fun Long.asLocalDateTime(timezoneId: String): LocalDateTime =
    LocalDateTime.ofEpochSecond(this, 0, ZoneId.of(timezoneId).rules.getOffset(Instant.now()))
