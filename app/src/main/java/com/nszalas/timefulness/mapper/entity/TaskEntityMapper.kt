package com.nszalas.timefulness.mapper.entity

import com.nszalas.timefulness.domain.model.Task
import com.nszalas.timefulness.infrastructure.local.entity.TaskEntity

class TaskEntityMapper {
    operator fun invoke(task: Task) = with(task) {
        TaskEntity(
            userId = userId.orEmpty(),
            title = title.orEmpty(),
            startTimestamp = startTimestamp ?: 0,
            endTimestamp = endTimestamp ?: 0,
            timezoneId = timezoneId.orEmpty(),
            categoryId = categoryId ?: 0,
            completed = completed
        )
    }
}