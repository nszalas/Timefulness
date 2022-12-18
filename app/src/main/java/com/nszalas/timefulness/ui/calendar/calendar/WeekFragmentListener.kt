package com.nszalas.timefulness.ui.calendar.calendar

import com.nszalas.timefulness.ui.model.TaskWithCategoryUI

interface WeekFragmentListener {
    fun scrollTo(y: Int)

    fun updateHoursTopMargin(margin: Int)

    fun getCurrScrollY(): Int

    fun updateRowHeight(rowHeight: Int)

    fun getFullFragmentHeight(): Int

    fun eventSingleTouch(task: TaskWithCategoryUI?)
}
