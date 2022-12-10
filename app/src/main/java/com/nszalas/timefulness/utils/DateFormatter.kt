package com.nszalas.timefulness.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class DateFormatter {
    private val dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)

    fun parseDateToString(year: Int, month: Int, dayOfMonth: Int): String =
        parseDate(year, month, dayOfMonth).format(dateFormatter)

    fun parseDate(year: Int, month: Int, dayOfMonth: Int): LocalDate =
        LocalDate.of(year, month, dayOfMonth)

    fun parseDate(date: String): LocalDate = LocalDate.parse(date, dateFormatter)

    fun formatDate(date: LocalDate): String? = try {
        date.format(dateFormatter)
    } catch (e: Exception) {
        null
    }

    companion object {
        private const val DATE_FORMAT_PATTERN = "yyyy-MM-dd"
    }
}