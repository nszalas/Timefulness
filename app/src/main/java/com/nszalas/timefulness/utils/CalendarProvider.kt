package com.nszalas.timefulness.utils

import android.icu.util.Calendar
import javax.inject.Inject

class CalendarProvider @Inject constructor() {
    operator fun invoke(): Calendar = Calendar.getInstance()
}