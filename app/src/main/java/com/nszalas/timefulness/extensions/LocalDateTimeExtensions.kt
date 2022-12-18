package com.nszalas.timefulness.extensions

import java.time.*

fun LocalDateTime.asTimestamp(): Long =
    this.toEpochSecond(ZoneOffset.systemDefault().rules.getOffset(Instant.now()))

fun LocalDateTime.millis(): Long =
    this.toEpochSecond(ZoneOffset.systemDefault().rules.getOffset(Instant.now())) * 1000

fun LocalDate.atStartOfNextDay(): LocalDateTime = this.plusDays(1).atStartOfDay()

fun Long.asLocalDateTime(timezoneId: String): LocalDateTime =
    LocalDateTime.ofEpochSecond(this, 0, ZoneId.of(timezoneId).rules.getOffset(Instant.now()))
