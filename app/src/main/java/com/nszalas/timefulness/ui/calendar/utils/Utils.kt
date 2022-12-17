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


// hide the actual notification from the top bar
// todo notification stuff
//fun Context.cancelNotification(id: Long) {
//    (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(id.toInt())
//}
//
//fun Context.getNotificationIntent(event: Event): PendingIntent {
//    val intent = Intent(this, NotificationReceiver::class.java)
//    intent.putExtra(EVENT_ID, event.id)
//    intent.putExtra(EVENT_OCCURRENCE_TS, event.startTS)
//    return PendingIntent.getBroadcast(this, event.id.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
//}
//
//fun Context.cancelPendingIntent(id: Long) {
//    val intent = Intent(this, NotificationReceiver::class.java)
//    PendingIntent.getBroadcast(this, id.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE).cancel()
//}

fun Context.getWeeklyViewItemHeight(): Float {
    return resources.getDimension(R.dimen.weekly_view_row_height)
}

fun Context.getDatesWeekDateTime(date: DateTime): String {
    val currentOffsetHours = TimeZone.getDefault().rawOffset / 1000 / 60 / 60

    // not great, not terrible
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