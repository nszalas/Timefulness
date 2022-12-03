package com.nszalas.timefulness.ui.profile

import com.boyzdroizy.simpleandroidbarchart.model.ChartData

data class ProfileViewState(
    val imageUrl: String? = null,
    val username: String = "",
    val email: String = "",
    val taskCompletedCount: Int = 0,
    val taskAllCount: Int = 0,
    val percentage: Int = 0,
    val statistics: List<ChartData> = emptyList(),
    val isLoading: Boolean = false
)