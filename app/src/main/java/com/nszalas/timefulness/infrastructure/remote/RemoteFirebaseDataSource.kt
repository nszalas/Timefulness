package com.nszalas.timefulness.infrastructure.remote

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteFirebaseDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient,
) {
    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    suspend fun logout(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            googleSignInClient.signOut().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signInWithCredential(credential: AuthCredential): Result<Unit> {
        return try {
            firebaseAuth.signInWithCredential(credential).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
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