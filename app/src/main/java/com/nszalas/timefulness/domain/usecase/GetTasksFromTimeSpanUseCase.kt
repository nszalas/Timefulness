package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.mapper.ui.TaskWithCategoryUIMapper
import com.nszalas.timefulness.repository.FirebaseRepository
import com.nszalas.timefulness.repository.TaskRepository
import com.nszalas.timefulness.ui.model.TaskWithCategoryUI
import javax.inject.Inject

class GetTasksFromTimeSpanUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val firebaseRepository: FirebaseRepository,
    private val mapper: TaskWithCategoryUIMapper,
) {
    suspend operator fun invoke(startTimestamp: Long, endTimestamp: Long): List<TaskWithCategoryUI> {
        val user = firebaseRepository.getCurrentUser() ?: return emptyList()

        return taskRepository.getTasksForUserBetweenTimestamps(
            startTimestamp,
            endTimestamp,
            user.id
        ).map { mapper(it) }
    }
}