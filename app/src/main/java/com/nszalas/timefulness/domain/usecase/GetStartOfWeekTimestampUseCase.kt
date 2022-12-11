package com.nszalas.timefulness.domain.usecase

import android.icu.util.Calendar
import com.nszalas.timefulness.utils.CalendarProvider
import java.time.LocalDate
import javax.inject.Inject

class GetStartOfWeekTimestampUseCase @Inject constructor(
    private val calendar: CalendarProvider
) {
    operator fun invoke(date: LocalDate): Long {
        val calendar = calendar()
        calendar.apply {
            set(date.year, date.monthValue, date.dayOfMonth)
            set(Calendar.HOUR_OF_DAY, 0)
            clear(Calendar.MINUTE)
            clear(Calendar.SECOND)
            clear(Calendar.MILLISECOND)
            set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
        }
        return calendar.timeInMillis / 1000
    }
}