package com.nszalas.timefulness.ui.signUp

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.nszalas.timefulness.domain.error.AuthenticationException
import com.nszalas.timefulness.domain.error.InvalidEmailException
import com.nszalas.timefulness.domain.error.InvalidPasswordException
import com.nszalas.timefulness.utils.LoginFormValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(): ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val validator = LoginFormValidator()

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        confirmPassword: String,
        result: (Result<Unit>) -> Unit
    ) {
        when {
            !validator.validateEmail(email) -> {
                result(Result.failure(InvalidEmailException()))
            }
            !validator.validatePassword(password, confirmPassword) -> {
                result(Result.failure(InvalidPasswordException()))
            }
            else -> {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
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