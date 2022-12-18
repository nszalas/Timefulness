package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.extensions.asTimestamp
import com.nszalas.timefulness.extensions.atStartOfNextDay
import com.nszalas.timefulness.mapper.ui.TaskWithCategoryUIMapper
import com.nszalas.timefulness.repository.AuthenticationRepository
import com.nszalas.timefulness.repository.TaskRepository
import com.nszalas.timefulness.ui.model.TaskWithCategoryUI
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetTasksForDateUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val mapper: TaskWithCategoryUIMapper,
    private val firebaseRepository: AuthenticationRepository,
) {
    suspend operator fun invoke(date: LocalDate): List<TaskWithCategoryUI> {
        val startTimestamp = date.atStartOfDay().asTimestamp()
        val endTimestamp = date.atStartOfNextDay().asTimestamp()
        val user = firebaseRepository.getCurrentUser() ?: return emptyList()

        return taskRepository.observeTasksForUserBetweenTimestamps(
            startTimestamp,
            endTimestamp,
            user.id
        ).map { tasks -> tasks.map { mapper(it) } }.first()
    }
}