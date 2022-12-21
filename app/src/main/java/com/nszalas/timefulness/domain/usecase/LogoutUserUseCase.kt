package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.repository.AuthenticationRepository
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(
    private val repository: AuthenticationRepository,
    private val cancelAllFutureTasksReminders: CancelAllFutureTasksRemindersUseCase
) {
    suspend operator fun invoke(): Result<Unit> {
        val user = repository.getCurrentUser() ?: throw(Exception())
        val result = repository.logout()

        if(result.isSuccess) {
            cancelAllFutureTasksReminders(user.id)
        }

        return result
    }
}