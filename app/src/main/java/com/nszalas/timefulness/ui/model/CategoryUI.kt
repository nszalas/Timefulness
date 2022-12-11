package com.nszalas.timefulness.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryUI(
    val id: Int,
    val name: String,
    val colorMain: Int,
    val colorText: Int,
    val emoji: String,
) : Parcelable