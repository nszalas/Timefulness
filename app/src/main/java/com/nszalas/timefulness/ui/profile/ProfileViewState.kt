package com.nszalas.timefulness.ui.profile

import com.boyzdroizy.simpleandroidbarchart.model.ChartData
import com.nszalas.timefulness.ui.model.UserUI

data class ProfileViewState(
    val user: UserUI? = null,
    val taskCompletedCount: Int = 0,
    val taskAllCount: Int = 0,
    val percentage: Int = 0,
    val statistics: List<ChartData> = emptyList(),
    val isLoading: Boolean = false
)