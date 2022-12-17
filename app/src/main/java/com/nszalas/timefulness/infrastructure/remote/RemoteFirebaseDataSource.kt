package com.nszalas.timefulness.infrastructure.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.nszalas.timefulness.domain.error.LogoutException
import javax.inject.Inject

class RemoteFirebaseDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) {
    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    fun logout(onResult: (Result<Unit>) -> Unit) {
        firebaseAuth.signOut()
        getCurrentUser()
            ?.run {
                onResult(Result.failure(LogoutException()))
            }
            ?: run {
                onResult(Result.success(Unit))
            }
    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onResult: (Result<Unit>) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onResult(Result.success(Unit))
            }.addOnFailureListener { exception ->
                onResult(Result.failure(exception))
            }
    }

    fun registerWithEmailAndPassword(
        email: String,
        password: String,
        onResult: (Result<Unit>) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onResult(Result.success(Unit))
            }.addOnFailureListener { exception ->
                onResult(Result.failure(exception))
            }
    }

    fun updateUserDisplayName(
        name: String,
        onResult: (Result<Unit>) -> Unit
    ) {
        val user = getCurrentUser() ?: return

        val update = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()

        user.updateProfile(update)
            .addOnSuccessListener {
                onResult(Result.success(Unit))
            }.addOnFailureListener { exception ->
                onResult(Result.failure(exception))
            }
    }
}