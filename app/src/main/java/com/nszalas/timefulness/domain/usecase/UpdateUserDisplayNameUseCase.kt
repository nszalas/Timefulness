package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.repository.FirebaseRepository
import javax.inject.Inject

class UpdateUserDisplayNameUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    operator fun invoke(name: String, onResult: (Result<Unit>) -> Unit) =
        repository.updateUserDisplayName(name, onResult)
}