package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.domain.model.Task
import com.nszalas.timefulness.repository.NotificationRepository
import javax.inject.Inject

class CancelTaskReminderUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(task: Task) {
        notificationRepository.cancelTaskReminder(task)
    }
}