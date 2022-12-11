package com.nszalas.timefulness.utils

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TimeFormatter @Inject constructor() {
    private val timeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT_PATTERN)

    fun parseTimeToString(hours: Int, minutes: Int): String =
        parseTime(hours, minutes).format(timeFormatter)

    fun parseTime(hours: Int, minutes: Int): LocalTime =
        LocalTime.of(hours, minutes)

    fun parseTime(time: String?): LocalTime = LocalTime.parse(time, timeFormatter)

    fun formatTime(time: LocalTime): String? = try {
        time.format(timeFormatter)
    } catch (e: Exception) {
        null
    }

    companion object {
        private const val TIME_FORMAT_PATTERN = "HH:mm"
    }
}