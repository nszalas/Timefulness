package com.nszalas.timefulness.domain.model

data class NotificationData(
    val title: String?,
    val body: String?,
    val alternativeContent: String?,
    val smallIconRes: Int?,
)