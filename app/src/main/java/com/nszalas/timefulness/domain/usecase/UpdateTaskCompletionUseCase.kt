package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.extensions.log
import com.nszalas.timefulness.ui.model.TaskUI
import javax.inject.Inject

class UpdateTaskCompletionUseCase @Inject constructor(
    private val updateTask: UpdateTaskUseCase,
    private val cancelTaskReminder: CancelTaskReminderUseCase,
    private val setTaskReminder: SetTaskReminderUseCase,
) {
    suspend operator fun invoke(task: TaskUI, completed: Boolean) {
        log("$task -> $completed")
        if(completed) {
            cancelTaskReminder(task)
        } else {
            setTaskReminder(task.copy(completed = false))
        }
        updateTask(task.copy(completed = completed))
    }
}