package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.domain.model.Task
import com.nszalas.timefulness.mapper.domain.TaskFromUIMapper
import com.nszalas.timefulness.repository.NotificationRepository
import com.nszalas.timefulness.ui.model.TaskUI
import javax.inject.Inject

class CancelTaskReminderUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val taskMapper: TaskFromUIMapper,
) {
    operator fun invoke(task: Task) {
        notificationRepository.cancelTaskReminder(task)
    }

    operator fun invoke(taskUI: TaskUI) {
        notificationRepository.cancelTaskReminder(taskMapper(taskUI))
    }
}