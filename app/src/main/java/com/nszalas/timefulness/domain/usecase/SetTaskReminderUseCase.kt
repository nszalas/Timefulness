package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.domain.model.Task
import com.nszalas.timefulness.extensions.asLocalDateTime
import com.nszalas.timefulness.extensions.millis
import com.nszalas.timefulness.mapper.domain.TaskFromUIMapper
import com.nszalas.timefulness.repository.NotificationRepository
import com.nszalas.timefulness.ui.model.TaskUI
import javax.inject.Inject

class SetTaskReminderUseCase @Inject constructor(
    private val repository: NotificationRepository,
    private val taskMapper: TaskFromUIMapper,
) {
    operator fun invoke(task: Task, remindMinutesBeforeTask: Int = 10) {
        task.startTimestamp ?: return
        task.timezoneId ?: return

        val notificationTime =
            task.startTimestamp.asLocalDateTime(task.timezoneId)
                .minusMinutes(remindMinutesBeforeTask.toLong())
                .withSecond(0)
        repository.setTaskReminder(triggerAtMillis = notificationTime.millis(), task)
    }

    operator fun invoke(task: TaskUI, remindMinutesBeforeTask: Int = 10) {
        val notificationTime =
            task.startTimestamp.asLocalDateTime(task.timezoneId)
                .minusMinutes(remindMinutesBeforeTask.toLong())
                .withSecond(0)
        repository.setTaskReminder(triggerAtMillis = notificationTime.millis(), taskMapper(task))
    }
}