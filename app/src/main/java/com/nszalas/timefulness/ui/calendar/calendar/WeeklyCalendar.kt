package com.nszalas.timefulness.ui.calendar.calendar

import com.nszalas.timefulness.ui.model.TaskWithCategoryUI

interface WeeklyCalendar {
    fun updateWeeklyCalendar(tasks: ArrayList<TaskWithCategoryUI>)
}
