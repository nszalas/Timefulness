package com.nszalas.timefulness.infrastructure.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nszalas.timefulness.domain.error.AuthenticationException
import javax.inject.Inject

class RemoteFirebaseDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) {
    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    fun signInWithEmailAndPassword(email: String, password: String, onResult: (Result<Unit>) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onResult(Result.success(Unit))
            }.addOnFailureListener { exception ->
                onResult(Result.failure(exception))
            }
    }

    fun registerWithEmailAndPassword(email: String, password: String, onResult: (Result<Unit>) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onResult(Result.success(Unit))
            }.addOnFailureListener { exception ->
                onResult(Result.failure(exception))
            }
    }
}