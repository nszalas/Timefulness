package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.extensions.asTimestamp
import com.nszalas.timefulness.extensions.atEndOfDay
import com.nszalas.timefulness.mapper.ui.TaskWithCategoryUIMapper
import com.nszalas.timefulness.repository.TaskRepository
import com.nszalas.timefulness.ui.model.TaskWithCategoryUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetTasksForDateUseCase @Inject constructor(
    private val repository: TaskRepository,
    private val mapper: TaskWithCategoryUIMapper
) {
    suspend operator fun invoke(date: LocalDate): List<TaskWithCategoryUI> {
        val startTimestamp = date.atStartOfDay().asTimestamp()
        val endTimestamp = date.atEndOfDay().asTimestamp()

        return repository.observeTasksFromTo(startTimestamp, endTimestamp).map { list ->
            list.map { mapper(it) }
        }.first()
    }
}