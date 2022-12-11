package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.repository.TaskRepository
import javax.inject.Inject

class GetAllTasksUseCase @Inject constructor(
    private val repository: TaskRepository,
) {
    suspend operator fun invoke() = repository.getAll()
}