package com.nszalas.timefulness.domain.usecase

import com.google.firebase.auth.AuthCredential
import com.nszalas.timefulness.repository.AuthenticationRepository
import javax.inject.Inject

class SignInWithCredentialUseCase @Inject constructor(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(credential: AuthCredential) =
        repository.signInWithCredential(credential)
}