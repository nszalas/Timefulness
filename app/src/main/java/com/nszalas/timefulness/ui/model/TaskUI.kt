package com.nszalas.timefulness.ui.model

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
)