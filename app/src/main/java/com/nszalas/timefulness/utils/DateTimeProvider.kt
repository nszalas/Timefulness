package com.nszalas.timefulness.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class DateTimeProvider {
    fun currentDateTime(): LocalDateTime = LocalDateTime.now()
    fun currentDate(): LocalDate = LocalDate.now()
    fun currentTime(): LocalTime = LocalTime.now()
}