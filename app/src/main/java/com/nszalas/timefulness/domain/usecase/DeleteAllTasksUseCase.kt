package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.repository.TaskRepository
import javax.inject.Inject

class DeleteAllTasksUseCase @Inject constructor(
    private val repository: TaskRepository,
) {
    suspend operator fun invoke() = repository.deleteAll()
}