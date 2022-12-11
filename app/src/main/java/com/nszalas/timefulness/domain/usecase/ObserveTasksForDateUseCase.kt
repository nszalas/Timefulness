package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.extensions.asTimestamp
import com.nszalas.timefulness.extensions.atStartOfNextDay
import com.nszalas.timefulness.mapper.ui.TaskWithCategoryUIMapper
import com.nszalas.timefulness.repository.TaskRepository
import com.nszalas.timefulness.ui.model.TaskWithCategoryUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class ObserveTasksForDateUseCase @Inject constructor(
    private val repository: TaskRepository,
    private val mapper: TaskWithCategoryUIMapper,
) {
    operator fun invoke(date: LocalDate): Flow<List<TaskWithCategoryUI>> {
        val startTimestamp = date.atStartOfDay().asTimestamp()
        val endTimestamp = date.atStartOfNextDay().asTimestamp()

        return repository.observeTasksFromTo(startTimestamp, endTimestamp).map { list ->
            list.map { mapper(it) }
        }
    }
}