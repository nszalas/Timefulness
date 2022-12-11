package com.nszalas.timefulness.ui.signIn

import androidx.lifecycle.ViewModel
import com.nszalas.timefulness.domain.error.AuthenticationException
import com.nszalas.timefulness.domain.error.InvalidEmailException
import com.nszalas.timefulness.domain.error.InvalidPasswordException
import com.nszalas.timefulness.domain.usecase.SignInWithEmailAndPasswordUseCase
import com.nszalas.timefulness.utils.LoginFormValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val validator: LoginFormValidator,
    private val signIn: SignInWithEmailAndPasswordUseCase,
) : ViewModel() {

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onResult: (Result<Unit>) -> Unit
    ) {
        when {
            !validator.validateEmail(email) -> {
                onResult(Result.failure(InvalidEmailException()))
            }
            !validator.validatePassword(password) -> {
                onResult(Result.failure(InvalidPasswordException()))
            }
            else -> {
                signIn(email, password) { result ->
                    if(result.isFailure) {
                        onResult(Result.failure(AuthenticationException()))
                    } else {
                        onResult(result)
                    }
                }
            }
        }
    }

}
