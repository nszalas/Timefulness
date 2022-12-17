package com.nszalas.timefulness.ui.calendar.utils

const val EVENT_ID = "event_id"
const val EVENT_OCCURRENCE_TS = "event_occurrence_ts"
const val WEEK_START_TIMESTAMP = "week_start_timestamp"
const val REGULAR_EVENT_TYPE_ID = 1L

const val REMINDER_OFF = -1

const val OTHER_EVENT = 0

const val TYPE_EVENT = 0
const val TYPE_TASK = 1

const val TWELVE_HOURS = 43200
const val DAY = 86400
const val WEEK = 604800
const val MONTH = 2592001    // exact value not taken into account, Joda is used for adding months and years
const val YEAR = 31536000

// Shared Preferences
const val DISPLAY_EVENT_TYPES = "display_event_types"
const val DIM_PAST_EVENTS = "dim_past_events"
const val DIM_COMPLETED_TASKS = "dim_completed_tasks"

// repeat_rule for monthly and yearly repetition
const val REPEAT_SAME_DAY = 1                           // i.e. 25th every month, or 3rd june (if yearly repetition)
const val REPEAT_ORDER_WEEKDAY_USE_LAST = 2             // i.e. every last sunday. 4th if a month has 4 sundays, 5th if 5 (or last sunday in june, if yearly)
const val REPEAT_LAST_DAY = 3                           // i.e. every last day of the month
const val REPEAT_ORDER_WEEKDAY = 4                      // i.e. every 4th sunday, even if a month has 4 sundays only (will stay 4th even at months with 5)

// special event and task flags
const val FLAG_TASK_COMPLETED = 8

fun getNowSeconds() = System.currentTimeMillis() / 1000L

fun isWeekend(i: Int, isSundayFirst: Boolean): Boolean {
    return if (isSundayFirst) {
        i == 0 || i == 6 || i == 7 || i == 13
    } else {
        i == 5 || i == 6 || i == 12 || i == 13
    }
}
