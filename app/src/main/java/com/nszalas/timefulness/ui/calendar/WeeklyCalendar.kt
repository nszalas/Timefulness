package com.nszalas.timefulness.ui.calendar

import com.nszalas.timefulness.ui.calendar.room.models.Event

interface WeeklyCalendar {
    fun updateWeeklyCalendar(events: ArrayList<Event>)
}
