package com.nszalas.timefulness.ui.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nszalas.timefulness.domain.error.AuthenticationException
import com.nszalas.timefulness.domain.error.InvalidEmailException
import com.nszalas.timefulness.domain.error.InvalidPasswordException
import com.nszalas.timefulness.domain.usecase.SignInWithEmailAndPasswordUseCase
import com.nszalas.timefulness.extensions.EventsChannel
import com.nszalas.timefulness.extensions.mutate
import com.nszalas.timefulness.utils.LoginFormValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val validator: LoginFormValidator,
    private val signIn: SignInWithEmailAndPasswordUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SignInViewState())
    val state: StateFlow<SignInViewState> = _state.asStateFlow()

    private val _event = EventsChannel<SignInViewEvent>()
    val event: Flow<SignInViewEvent> = _event.receiveAsFlow()

    fun onSignIn() {
        signInWithEmailAndPassword()
    }

    fun onEmailEntered(email: String?) {
        viewModelScope.launch {
            val error = if (validator.isEmailValid(email)) {
                null
            } else {
                InvalidEmailException().message
            }
            val buttonEnabled = error == null && state.value.passwordError == null
            _state.mutate {
                copy(
                    email = email,
                    emailError = error,
                    buttonEnabled = buttonEnabled
                )
            }
        }
    }

    fun onPasswordEntered(password: String?) {
        viewModelScope.launch {
            val error = if (validator.isPasswordValid(password)) {
                null
            } else {
                InvalidPasswordException().message
            }
            val buttonEnabled =
                error == null && state.value.emailError == null
            _state.mutate {
                copy(
                    password = password,
                    passwordError = error,
                    buttonEnabled = buttonEnabled
                )
            }
        }
    }

    private fun signInWithEmailAndPassword() {
        viewModelScope.launch {
            _state.mutate { copy(isLoading = true) }
            val email = state.value.email ?: return@launch
            val password = state.value.password ?: return@launch
            signIn(email, password) { result ->
                if (result.isFailure) {
                    _event.trySend(SignInViewEvent.Error(AuthenticationException().message))
                } else {
                    _event.trySend(SignInViewEvent.UserLoggedIn)
                }
                _state.mutate { copy(isLoading = false) }
            }
        }
    }
}
