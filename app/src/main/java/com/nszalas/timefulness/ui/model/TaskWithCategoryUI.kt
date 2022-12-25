package com.nszalas.timefulness.ui.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class TaskWithCategoryUI(
    val task: TaskUI,
    val category: CategoryUI,
) : Parcelable