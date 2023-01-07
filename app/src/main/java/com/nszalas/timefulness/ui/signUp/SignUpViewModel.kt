package com.nszalas.timefulness.ui.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nszalas.timefulness.domain.error.AuthenticationException
import com.nszalas.timefulness.domain.error.InvalidEmailException
import com.nszalas.timefulness.domain.error.InvalidPasswordException
import com.nszalas.timefulness.domain.error.MissingTacException
import com.nszalas.timefulness.domain.usecase.RegisterWithEmailAndPasswordUseCase
import com.nszalas.timefulness.domain.usecase.UpdateUserDisplayNameUseCase
import com.nszalas.timefulness.extensions.EventsChannel
import com.nszalas.timefulness.extensions.mutate
import com.nszalas.timefulness.utils.LoginFormValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val validator: LoginFormValidator,
    private val register: RegisterWithEmailAndPasswordUseCase,
    private val updateUserName: UpdateUserDisplayNameUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpViewState())
    val state: StateFlow<SignUpViewState> = _state.asStateFlow()

    private val _event = EventsChannel<SignUpViewEvent>()
    val event: Flow<SignUpViewEvent> = _event.receiveAsFlow()

    fun onTacChecked(checked: Boolean) {
        viewModelScope.launch {
            _state.mutate { copy(agreedToTAC = checked) }
        }
    }

    fun createUser(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
    ) {
        viewModelScope.launch {
            _state.mutate { copy(isLoading = true) }
            when {
                !validator.isEmailValid(email) -> {
                    _event.trySend(SignUpViewEvent.Error(InvalidEmailException().message))
                    _state.mutate { copy(isLoading = false) }
                }
                !validator.isPasswordValid(password, confirmPassword) -> {
                    _event.trySend(SignUpViewEvent.Error(InvalidPasswordException().message))
                    _state.mutate { copy(isLoading = false) }
                }
                !state.value.agreedToTAC -> {
                    _event.trySend(SignUpViewEvent.Error(MissingTacException().message))
                    _state.mutate { copy(isLoading = false) }
                }
                else -> {
                    try {
                        if (register(email, password).isSuccess) {
                            if (setUserName(name).isSuccess) {
                                _event.trySend(SignUpViewEvent.SignUpSuccess)
                                _state.mutate { copy(isLoading = false) }
                            } else {
                                _event.trySend(SignUpViewEvent.Error(AuthenticationException().message))
                                _state.mutate { copy(isLoading = false) }
                            }
                        } else {
                            _event.trySend(SignUpViewEvent.Error(AuthenticationException().message))
                            _state.mutate { copy(isLoading = false) }
                        }
                    } catch (e: Exception) {
                        _state.mutate { copy(isLoading = false) }
                        _event.trySend(SignUpViewEvent.Error(AuthenticationException().message))
                    }
                }
            }
        }
    }

    private suspend fun setUserName(name: String): Result<Unit> = updateUserName(name)
}