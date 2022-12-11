package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.mapper.ui.TaskWithCategoryUIMapper
import com.nszalas.timefulness.repository.TaskRepository
import com.nszalas.timefulness.ui.model.TaskWithCategoryUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveAllTasksUseCase @Inject constructor(
    private val repository: TaskRepository,
    private val mapper: TaskWithCategoryUIMapper,
) {
    operator fun invoke(): Flow<List<TaskWithCategoryUI>> =
        repository.observeAllTasks().map { list -> list.map { mapper(it) } }
}