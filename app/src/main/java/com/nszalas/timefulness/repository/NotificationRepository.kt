package com.nszalas.timefulness.repository

import com.nszalas.timefulness.domain.model.NotificationData
import com.nszalas.timefulness.domain.model.Task
import com.nszalas.timefulness.infrastructure.local.LocalNotificationDataSource
import com.nszalas.timefulness.infrastructure.local.LocalSchedulingDataSource
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val schedulingDataSource: LocalSchedulingDataSource,
    private val notificationDataSource: LocalNotificationDataSource
) {

    fun sendNotification(notificationData: NotificationData) {
        notificationDataSource.sendNotification(notificationData)
    }

    fun setTaskReminder(triggerAtMillis: Long, task: Task) {
        schedulingDataSource.setTaskReminder(triggerAtMillis, task)
    }

    fun cancelTaskReminder(task: Task) {
        schedulingDataSource.cancelTaskReminder(task)
    }
}