package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.repository.AuthenticationRepository
import javax.inject.Inject

class SignInWithEmailAndPasswordUseCase @Inject constructor(
    private val repository: AuthenticationRepository,
    private val setAllFutureTasksReminders: SetAllFutureTasksRemindersUseCase,
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        val result = repository.signInWithEmailAndPassword(email, password)

        if (result.isSuccess) {
            setAllFutureTasksReminders()
        }

        return result
    }
}