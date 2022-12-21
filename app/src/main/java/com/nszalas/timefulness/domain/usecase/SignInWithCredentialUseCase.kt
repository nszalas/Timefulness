package com.nszalas.timefulness.domain.usecase

import com.google.firebase.auth.AuthCredential
import com.nszalas.timefulness.repository.AuthenticationRepository
import javax.inject.Inject

class SignInWithCredentialUseCase @Inject constructor(
    private val repository: AuthenticationRepository,
    private val setAllFutureTasksReminders: SetAllFutureTasksRemindersUseCase
) {
    suspend operator fun invoke(credential: AuthCredential): Result<Unit> {
        val result = repository.signInWithCredential(credential)

        if (result.isSuccess) {
            setAllFutureTasksReminders()
        }

        return result
    }

}