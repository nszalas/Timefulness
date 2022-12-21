package com.nszalas.timefulness.infrastructure.remote

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.nszalas.timefulness.domain.error.NoLoggedInUserException
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

    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun registerWithEmailAndPassword(
        email: String,
        password: String,
    ): Result<Unit> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserDisplayName(
        name: String,
    ): Result<Unit> {
        val user = getCurrentUser() ?: return Result.failure(NoLoggedInUserException())

        val update = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()

        return try {
            user.updateProfile(update).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}