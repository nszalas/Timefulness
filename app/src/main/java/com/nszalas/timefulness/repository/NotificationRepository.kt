package com.nszalas.timefulness.repository

import com.nszalas.timefulness.domain.model.NotificationData
import com.nszalas.timefulness.infrastructure.local.LocalNotificationDataSource
import com.nszalas.timefulness.infrastructure.local.LocalSchedulingDataSource
import java.util.*
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val schedulingDataSource: LocalSchedulingDataSource,
    private val notificationDataSource: LocalNotificationDataSource
) {

    fun sendNotification(notificationData: NotificationData) {
        notificationDataSource.sendNotification(notificationData)
    }

    fun closeNotification(notificationId: Int) {
        notificationDataSource.closeNotification(notificationId)
    }

    fun getNotificationDate(notificationId: Int): Date? =
        notificationDataSource.getNotificationDate(notificationId)

    fun setTaskReminder(triggerAtMillis: Long) {
        schedulingDataSource.setTaskReminder(triggerAtMillis)
    }

    fun cancelTaskReminder() {
        schedulingDataSource.cancelTaskReminder()
    }
}