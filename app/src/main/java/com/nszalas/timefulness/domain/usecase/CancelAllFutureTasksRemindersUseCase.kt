package com.nszalas.timefulness.domain.usecase

import javax.inject.Inject

class CancelAllFutureTasksRemindersUseCase @Inject constructor(
    private val getFutureUncompletedTasks: GetFutureUncompletedTasksUseCase,
    private val cancelTaskReminderUseCase: CancelTaskReminderUseCase,
) {
    suspend operator fun invoke(userId: String) {
        getFutureUncompletedTasks(userId).map {
            cancelTaskReminderUseCase(it.task)
        }
    }
}