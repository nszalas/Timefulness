package com.nszalas.timefulness.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showToast(message: String, length: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, message, length).show()

fun Context.showToast(@StringRes messageId: Int, length: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, messageId, length).show()