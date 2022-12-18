package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.repository.AuthenticationRepository
import javax.inject.Inject

class UpdateUserDisplayNameUseCase @Inject constructor(
    private val repository: AuthenticationRepository
) {
    operator fun invoke(name: String, onResult: (Result<Unit>) -> Unit) =
        repository.updateUserDisplayName(name, onResult)
}