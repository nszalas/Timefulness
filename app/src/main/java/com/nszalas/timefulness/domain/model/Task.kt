package com.nszalas.timefulness.domain.model

data class Task(
    val id: Int = 0,
    val userId: String?,
    val title: String?,
    val startTimestamp: Long?,
    val endTimestamp: Long?,
    val timezoneId: String?,
    val categoryId: Int?,
    val repeat: Boolean = false,
    val completed: Boolean = false,
)