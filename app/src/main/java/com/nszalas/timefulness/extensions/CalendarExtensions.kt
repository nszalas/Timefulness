package com.nszalas.timefulness.extensions

import android.icu.util.Calendar

fun Calendar.getTimeInSeconds() = this.timeInMillis / 1000
