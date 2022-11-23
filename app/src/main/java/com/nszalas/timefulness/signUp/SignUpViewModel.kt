package com.nszalas.timefulness.signUp

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.nszalas.timefulness.error.InvalidEmailException
import com.nszalas.timefulness.error.InvalidPasswordException
import com.nszalas.timefulness.utils.LoginFormValidator

class SignUpViewModel : ViewModel() {
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
                    .addOnCompleteListener {
                        result(Result.success(Unit))
                    }
            }
        }
    }
}