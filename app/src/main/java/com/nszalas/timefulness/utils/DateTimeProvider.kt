package com.nszalas.timefulness.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

class DateTimeProvider @Inject constructor() {
    fun currentDateTime(): LocalDateTime = LocalDateTime.now()
    fun currentDate(): LocalDate = LocalDate.now()
    fun currentTime(): LocalTime = LocalTime.now()
}