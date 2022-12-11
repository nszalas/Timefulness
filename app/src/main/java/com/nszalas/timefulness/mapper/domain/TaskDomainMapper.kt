package com.nszalas.timefulness.mapper.domain

import com.nszalas.timefulness.domain.model.Task
import com.nszalas.timefulness.infrastructure.local.entity.TaskEntity
import com.nszalas.timefulness.ui.model.TaskUI

class TaskDomainMapper {
    operator fun invoke(task: TaskEntity) = with(task) {
        Task(
            id = id,
            userId = userId,
            title = title,
            startTimestamp = startTimestamp,
            endTimestamp = endTimestamp,
            timezoneId = timezoneId,
            categoryId = categoryId,
            repeat = repeat,
            completed = completed
        )
    }
}

class TaskFromUIMapper {
    operator fun invoke(task: TaskUI) = with(task) {
        Task(
            id = id,
            userId = userId,
            title = title,
            startTimestamp = startTimestamp,
            endTimestamp = endTimestamp,
            timezoneId = timezoneId,
            categoryId = categoryId,
            repeat = repeat,
            completed = completed
        )
    }
}