package com.nszalas.timefulness.ui.calendar.utils

import android.content.Context
import android.graphics.Paint
import android.util.Range
import android.widget.TextView
import com.nszalas.timefulness.R
import com.simplemobiletools.commons.extensions.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.*

fun DateTime.seconds() = millis / 1000L

fun Context.getWeeklyViewItemHeight(): Float {
    return resources.getDimension(R.dimen.weekly_view_row_height)
}

fun getDatesWeekDateTime(date: DateTime): String {
    val currentOffsetHours = TimeZone.getDefault().rawOffset / 1000 / 60 / 60

    val useHours = if (currentOffsetHours >= 10) 8 else 12
    var thisWeek =
        date.withZone(DateTimeZone.UTC).withDayOfWeek(1).withHourOfDay(useHours).minusDays(
            if (Calendar.getInstance().firstDayOfWeek == Calendar.SUNDAY) 1 else 0
        )
    if (date.minusDays(7).seconds() > thisWeek.seconds()) {
        thisWeek = thisWeek.plusDays(7)
    }
    return thisWeek.toString()
}

// range

fun Range<Int>.intersects(other: Range<Int>) =
    (upper >= other.lower && lower <= other.upper) || (other.upper >= lower && other.lower <= upper)

// text view

fun TextView.checkViewStrikeThrough(addFlag: Boolean) {
    paintFlags = if (addFlag) {
        paintFlags.addBit(Paint.STRIKE_THRU_TEXT_FLAG)
    } else {
        paintFlags.removeBit(Paint.STRIKE_THRU_TEXT_FLAG)
    }
}
