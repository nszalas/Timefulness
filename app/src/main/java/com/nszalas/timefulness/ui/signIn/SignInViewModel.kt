package com.nszalas.timefulness.ui.signIn

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.nszalas.timefulness.error.AuthenticationException
import com.nszalas.timefulness.error.InvalidEmailException
import com.nszalas.timefulness.error.InvalidPasswordException
import com.nszalas.timefulness.utils.LoginFormValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor() : ViewModel() {

    private var firebaseAuth = FirebaseAuth.getInstance()
    private val validator = LoginFormValidator()

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        result: (Result<Unit>) -> Unit
    ) {
        when {
            !validator.validateEmail(email) -> {
                result(Result.failure(InvalidEmailException()))
            }
            !validator.validatePassword(password) -> {
                result(Result.failure(InvalidPasswordException()))
            }
            else -> {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        result(Result.success(Unit))
                    }
                    .addOnFailureListener {
                        result(Result.failure(AuthenticationException()))
                    }
            }
        }
    }

}
