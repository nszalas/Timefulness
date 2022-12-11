package com.nszalas.timefulness.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskUI(
    val id: Int,
    val userId: String,
    val title: String,
    val startTimestamp: Long,
    val endTimestamp: Long,
    val timezoneId: String,
    val categoryId: Int,
    val repeat: Boolean,
    val completed: Boolean,
) : Parcelable