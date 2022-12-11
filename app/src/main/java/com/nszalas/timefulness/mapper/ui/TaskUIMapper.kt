package com.nszalas.timefulness.mapper.ui

import com.nszalas.timefulness.domain.model.Task
import com.nszalas.timefulness.ui.model.TaskUI

class TaskUIMapper {
    operator fun invoke(task: Task) = with(task) {
        TaskUI(
            id = id,
            userId = userId.orEmpty(),
            title = title.orEmpty(),
            startTimestamp = startTimestamp ?: 0,
            endTimestamp = endTimestamp ?: 0,
            timezoneId = timezoneId.orEmpty(),
            categoryId = categoryId ?: 0,
            repeat = repeat,
            completed = completed
        )
    }
}