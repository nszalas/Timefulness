package com.nszalas.timefulness.mapper.ui

import com.nszalas.timefulness.domain.model.TaskWithCategory
import com.nszalas.timefulness.ui.model.TaskWithCategoryUI
import javax.inject.Inject

class TaskWithCategoryUIMapper @Inject constructor(
    private val taskMapper: TaskUIMapper,
    private val categoryMapper: CategoryUIMapper,
) {
    operator fun invoke(taskWithCategory: TaskWithCategory) = with(taskWithCategory) {
        TaskWithCategoryUI(
            task = taskMapper(task),
            category = categoryMapper(category),
        )
    }
}