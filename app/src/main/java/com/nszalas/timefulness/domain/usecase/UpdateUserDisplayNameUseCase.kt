package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.repository.AuthenticationRepository
import javax.inject.Inject

class UpdateUserDisplayNameUseCase @Inject constructor(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(name: String): Result<Unit> =
        repository.updateUserDisplayName(name)
}