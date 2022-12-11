package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.repository.FirebaseRepository
import javax.inject.Inject

class RegisterWithEmailAndPasswordUseCase @Inject constructor(
    private val repository: FirebaseRepository,
) {
    operator fun invoke(email: String, password: String, onResult: (Result<Unit>) -> Unit) {
        repository.registerWithEmailAndPassword(email, password) { result -> onResult(result) }
    }
}