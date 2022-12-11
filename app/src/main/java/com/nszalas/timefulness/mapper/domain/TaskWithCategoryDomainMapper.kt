package com.nszalas.timefulness.mapper.domain

import com.nszalas.timefulness.domain.model.TaskWithCategory
import com.nszalas.timefulness.infrastructure.local.entity.TaskWithCategoryEntity
import javax.inject.Inject

class TaskWithCategoryDomainMapper @Inject constructor(
    private val taskMapper: TaskDomainMapper,
    private val categoryMapper: CategoryDomainMapper
) {
    operator fun invoke(taskWithCategory: TaskWithCategoryEntity) = with(taskWithCategory) {
        TaskWithCategory(
            task = taskMapper(task),
            category = categoryMapper(category),
        )
    }
}