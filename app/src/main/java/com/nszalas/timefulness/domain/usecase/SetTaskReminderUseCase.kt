package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.extensions.millis
import com.nszalas.timefulness.repository.NotificationRepository
import java.time.LocalDateTime
import javax.inject.Inject

class SetTaskReminderUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(givenTime: LocalDateTime) {
        repository.setTaskReminder(triggerAtMillis = givenTime.millis())
    }
}