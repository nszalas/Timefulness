package com.nszalas.timefulness.extensions

import java.time.*

fun LocalDateTime.asTimestamp(): Long =
    this.toEpochSecond(ZoneOffset.systemDefault().rules.getOffset(Instant.now()))

fun LocalDate.atEndOfDay(): LocalDateTime = this.atTime(23, 59)

fun Long.asLocalDateTime(timezoneId: String): LocalDateTime =
    LocalDateTime.ofEpochSecond(this, 0, ZoneId.of(timezoneId).rules.getOffset(Instant.now()))
