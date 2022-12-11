package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.repository.FirebaseRepository
import javax.inject.Inject

class SignInWithEmailAndPasswordUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    operator fun invoke(email: String, password: String, onResult: (Result<Unit>) -> Unit) {
        repository.signInWithEmailAndPassword(email, password) { result -> onResult(result) }
    }
}