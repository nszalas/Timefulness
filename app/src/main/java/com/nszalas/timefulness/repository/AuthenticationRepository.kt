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

    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): Result<Unit> = dataSource.signInWithEmailAndPassword(email, password)

    suspend fun registerWithEmailAndPassword(
        email: String,
        password: String,
    ): Result<Unit> = dataSource.registerWithEmailAndPassword(email, password)

    suspend fun updateUserDisplayName(
        name: String,
    ): Result<Unit> = dataSource.updateUserDisplayName(name)
}