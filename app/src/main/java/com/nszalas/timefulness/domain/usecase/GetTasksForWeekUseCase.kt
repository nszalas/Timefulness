package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.mapper.ui.TaskWithCategoryUIMapper
import com.nszalas.timefulness.repository.TaskRepository
import com.nszalas.timefulness.ui.model.TaskWithCategoryUI
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetTasksForWeekUseCase @Inject constructor(
    private val getStartOfWeekTimestamp: GetStartOfWeekTimestampUseCase,
    private val getEndOfWeekTimestamp: GetEndOfWeekTimestampUseCase,
    private val repository: TaskRepository,
    private val mapper: TaskWithCategoryUIMapper,
) {
    suspend operator fun invoke(date: LocalDate): List<TaskWithCategoryUI> {
        val startTimestamp = getStartOfWeekTimestamp(date)
        val endTimestamp = getEndOfWeekTimestamp(date)

        return repository.observeTasksFromTo(startTimestamp, endTimestamp).map { list ->
            list.map { mapper(it) }
        }.first()
    }
}