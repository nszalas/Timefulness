package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.repository.FirebaseRepository
import javax.inject.Inject

class IsUserLoggedInUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    operator fun invoke(): Boolean = repository.getCurrentUser() != null
}