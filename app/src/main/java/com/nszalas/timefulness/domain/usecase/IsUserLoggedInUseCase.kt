package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.repository.AuthenticationRepository
import javax.inject.Inject

class IsUserLoggedInUseCase @Inject constructor(
    private val repository: AuthenticationRepository
) {
    operator fun invoke(): Boolean = repository.getCurrentUser() != null
}