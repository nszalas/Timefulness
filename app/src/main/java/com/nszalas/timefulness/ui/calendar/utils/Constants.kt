package com.nszalas.timefulness.ui.calendar.utils

const val EVENT_ID = "event_id"
const val WEEK_START_TIMESTAMP = "week_start_timestamp"

fun getNowSeconds() = System.currentTimeMillis() / 1000L
