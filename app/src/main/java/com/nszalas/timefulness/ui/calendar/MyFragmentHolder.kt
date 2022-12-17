package com.nszalas.timefulness.ui.calendar

import androidx.fragment.app.Fragment
import org.joda.time.DateTime

abstract class MyFragmentHolder : Fragment() {
    abstract fun goToToday()

    abstract fun showGoToDateDialog()

    abstract fun refreshEvents()

    abstract fun shouldGoToTodayBeVisible(): Boolean

    abstract fun getNewEventDayCode(): String

    abstract fun getCurrentDate(): DateTime?
}
