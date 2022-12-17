package com.nszalas.timefulness.ui.calendar

interface WeekFragmentListener {
    fun scrollTo(y: Int)

    fun updateHoursTopMargin(margin: Int)

    fun getCurrScrollY(): Int

    fun updateRowHeight(rowHeight: Int)

    fun getFullFragmentHeight(): Int

    fun eventSingleTouch(eventID: Long?)
}
