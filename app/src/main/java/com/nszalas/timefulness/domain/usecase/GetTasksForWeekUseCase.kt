package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.mapper.ui.TaskWithCategoryUIMapper
import com.nszalas.timefulness.repository.AuthenticationRepository
import com.nszalas.timefulness.repository.TaskRepository
import com.nszalas.timefulness.ui.model.TaskWithCategoryUI
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetTasksForWeekUseCase @Inject constructor(
    private val getStartOfWeekTimestamp: GetStartOfWeekTimestampUseCase,
    private val getEndOfWeekTimestamp: GetEndOfWeekTimestampUseCase,
    private val taskRepository: TaskRepository,
    private val firebaseRepository: AuthenticationRepository,
    private val mapper: TaskWithCategoryUIMapper,
) {
    suspend operator fun invoke(date: LocalDate): List<TaskWithCategoryUI> {
        val startTimestamp = getStartOfWeekTimestamp(date)
        val endTimestamp = getEndOfWeekTimestamp(date)
        val user = firebaseRepository.getCurrentUser() ?: return emptyList()

        return taskRepository.observeTasksForUserBetweenTimestamps(
            startTimestamp,
            endTimestamp,
            user.id
        ).map { tasks -> tasks.map { mapper(it) } }.first()
    }
}