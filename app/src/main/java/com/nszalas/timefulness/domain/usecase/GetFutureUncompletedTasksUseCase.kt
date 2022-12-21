package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.extensions.asTimestamp
import com.nszalas.timefulness.mapper.ui.TaskWithCategoryUIMapper
import com.nszalas.timefulness.repository.AuthenticationRepository
import com.nszalas.timefulness.repository.TaskRepository
import com.nszalas.timefulness.ui.model.TaskWithCategoryUI
import com.nszalas.timefulness.utils.DateTimeProvider
import javax.inject.Inject

class GetFutureUncompletedTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val dateTimeProvider: DateTimeProvider,
    private val authenticationRepository: AuthenticationRepository,
    private val taskWithCategoryUIMapper: TaskWithCategoryUIMapper
) {
    suspend operator fun invoke(userId: String? = null): List<TaskWithCategoryUI> {
        val user = authenticationRepository.getCurrentUser()

        val searchId = when {
            user != null -> user.id
            userId != null -> userId
            else -> return emptyList()
        }

        return taskRepository.getFutureUncompletedTasks(
            nowTimestamp = dateTimeProvider.currentDateTime().asTimestamp(),
            userId = searchId
        ).map { taskWithCategoryUIMapper(it) }
    }
}