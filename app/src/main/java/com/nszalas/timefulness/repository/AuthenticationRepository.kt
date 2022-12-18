package com.nszalas.timefulness.repository

import com.google.firebase.auth.AuthCredential
import com.nszalas.timefulness.domain.model.User
import com.nszalas.timefulness.infrastructure.remote.RemoteFirebaseDataSource
import com.nszalas.timefulness.mapper.domain.UserDomainMapper
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val dataSource: RemoteFirebaseDataSource,
    private val mapper: UserDomainMapper,
) {
    fun getCurrentUser(): User? = dataSource.getCurrentUser()?.let { mapper(it) }

    suspend fun logout(): Result<Unit> = dataSource.logout()

    suspend fun signInWithCredential(credential: AuthCredential): Result<Unit> =
        dataSource.signInWithCredential(credential)

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onResult: (Result<Unit>) -> Unit
    ) {
        dataSource.signInWithEmailAndPassword(email, password, onResult)
    }

    fun registerWithEmailAndPassword(
        email: String,
        password: String,
        onResult: (Result<Unit>) -> Unit
    ) {
        dataSource.registerWithEmailAndPassword(email, password, onResult)
    }

    fun updateUserDisplayName(
        name: String,
        onResult: (Result<Unit>) -> Unit
    ) {
        dataSource.updateUserDisplayName(name, onResult)
    }
}