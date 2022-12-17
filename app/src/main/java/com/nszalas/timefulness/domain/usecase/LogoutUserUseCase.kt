package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.repository.FirebaseRepository
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    operator fun invoke(onResult: (Result<Unit>) -> Unit) = repository.logout(onResult)
}