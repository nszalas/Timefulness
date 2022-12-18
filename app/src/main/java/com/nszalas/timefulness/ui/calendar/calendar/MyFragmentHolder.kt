package com.nszalas.timefulness.ui.calendar.calendar

import org.joda.time.DateTime

abstract class MyFragmentHolder : BaseFragment() {
    abstract fun goToToday()

    abstract fun showGoToDateDialog()

    abstract fun refreshEvents()

    abstract fun shouldGoToTodayBeVisible(): Boolean

    abstract fun getNewEventDayCode(): String

    abstract fun getCurrentDate(): DateTime?
}
