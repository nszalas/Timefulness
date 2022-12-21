package com.nszalas.timefulness.domain.usecase

import javax.inject.Inject

class SetAllFutureTasksRemindersUseCase @Inject constructor(
    private val getFutureUncompletedTasks: GetFutureUncompletedTasksUseCase,
    private val setTaskReminderUseCase: SetTaskReminderUseCase,
) {
    suspend operator fun invoke() {
        getFutureUncompletedTasks().map {
            setTaskReminderUseCase(it.task)
        }
    }
}