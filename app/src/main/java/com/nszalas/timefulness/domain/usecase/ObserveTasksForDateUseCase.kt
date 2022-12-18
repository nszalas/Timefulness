package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.extensions.asTimestamp
import com.nszalas.timefulness.extensions.atStartOfNextDay
import com.nszalas.timefulness.mapper.ui.TaskWithCategoryUIMapper
import com.nszalas.timefulness.repository.AuthenticationRepository
import com.nszalas.timefulness.repository.TaskRepository
import com.nszalas.timefulness.ui.model.TaskWithCategoryUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class ObserveTasksForDateUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val mapper: TaskWithCategoryUIMapper,
    private val firebaseRepository: AuthenticationRepository,
) {
    operator fun invoke(date: LocalDate): Flow<List<TaskWithCategoryUI>> {
        val startTimestamp = date.atStartOfDay().asTimestamp()
        val endTimestamp = date.atStartOfNextDay().asTimestamp()
        val user = firebaseRepository.getCurrentUser() ?: return flowOf(emptyList())

        return taskRepository.observeTasksForUserBetweenTimestamps(
            startTimestamp,
            endTimestamp,
            user.id
        ).map { tasks -> tasks.map { mapper(it) } }
    }
}