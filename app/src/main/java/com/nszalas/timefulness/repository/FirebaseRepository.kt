package com.nszalas.timefulness.repository

import com.nszalas.timefulness.domain.model.User
import com.nszalas.timefulness.infrastructure.remote.RemoteFirebaseDataSource
import com.nszalas.timefulness.mapper.domain.UserDomainMapper
import javax.inject.Inject

class FirebaseRepository @Inject constructor(
    private val dataSource: RemoteFirebaseDataSource,
    private val mapper: UserDomainMapper,
) {
    fun getCurrentUser(): User? = dataSource.getCurrentUser()?.let { mapper(it) }

    fun logout(onResult: (Result<Unit>) -> Unit) = dataSource.logout(onResult)

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onResult: (Result<Unit>) -> Unit
    ) {
        dataSource.signInWithEmailAndPassword(email, password) { result ->
            onResult(result)
        }
    }

    fun registerWithEmailAndPassword(
        email: String,
        password: String,
        onResult: (Result<Unit>) -> Unit
    ) {
        dataSource.registerWithEmailAndPassword(email, password) { result ->
            onResult(result)
        }
    }
}