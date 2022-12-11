package com.nszalas.timefulness.ui.signUp

import androidx.lifecycle.ViewModel
import com.nszalas.timefulness.domain.error.AuthenticationException
import com.nszalas.timefulness.domain.error.InvalidEmailException
import com.nszalas.timefulness.domain.error.InvalidPasswordException
import com.nszalas.timefulness.domain.usecase.RegisterWithEmailAndPasswordUseCase
import com.nszalas.timefulness.utils.LoginFormValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val validator: LoginFormValidator,
    private val register: RegisterWithEmailAndPasswordUseCase,
) : ViewModel() {

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        confirmPassword: String,
        onResult: (Result<Unit>) -> Unit
    ) {
        when {
            !validator.validateEmail(email) -> {
                onResult(Result.failure(InvalidEmailException()))
            }
            !validator.validatePassword(password, confirmPassword) -> {
                onResult(Result.failure(InvalidPasswordException()))
            }
            else -> {
                register(email, password) { result ->
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