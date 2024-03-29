package com.nszalas.timefulness.ui.calendar.calendar

import android.util.Range

data class EventWeeklyView(
    val range: Range<Int>,
    var slot: Int = 0,
    var slot_max: Int = 0,
    var collisions: ArrayList<Long> = ArrayList()
)
