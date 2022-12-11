package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskWithIdUseCase @Inject constructor(
    private val repository: TaskRepository,
) {
    suspend operator fun invoke(taskId: Int) = repository.deleteTaskWithId(taskId)
}