package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.domain.model.NotificationData
import com.nszalas.timefulness.repository.NotificationRepository
import javax.inject.Inject

class SendNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository,
) {
    operator fun invoke(notificationData: NotificationData) {
        repository.sendNotification(notificationData)
    }
}