package com.nszalas.timefulness.ui.signIn

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.nszalas.timefulness.domain.error.AuthenticationException
import com.nszalas.timefulness.domain.error.InvalidEmailException
import com.nszalas.timefulness.domain.error.InvalidPasswordException
import com.nszalas.timefulness.domain.usecase.GetCredentialFromAccountUseCase
import com.nszalas.timefulness.domain.usecase.SignInWithCredentialUseCase
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
    private val googleSignInClient: GoogleSignInClient,
    private val getCredentialFromAccount: GetCredentialFromAccountUseCase,
    private val signInWithCredential: SignInWithCredentialUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SignInViewState())
    val state: StateFlow<SignInViewState> = _state.asStateFlow()

    private val _event = EventsChannel<SignInViewEvent>()
    val event: Flow<SignInViewEvent> = _event.receiveAsFlow()

    fun onSignIn() {
        signInWithEmailAndPassword()
    }

    fun onGoogleSignIn() {
        viewModelScope.launch {
            getGoogleSignInIntent().let { intent ->
                _event.trySend(SignInViewEvent.LaunchActivityWithIntent(intent))
            }
        }
    }

    fun signInWithCredentialFromActivityResult(result: ActivityResult) {
        getCredentialFromActivityResult(result)?.let { credential ->
            signInWithAuthCredential(credential)
        }
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

    private fun getGoogleSignInIntent(): Intent = googleSignInClient.signInIntent

    private fun getCredentialFromActivityResult(result: ActivityResult): AuthCredential? {
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        return try {
            val account = task.getResult(ApiException::class.java)
            getCredentialFromAccount(account)
        } catch (e: ApiException) {
            _event.trySend(SignInViewEvent.Error(e.message))
            null
        }
    }

    private fun signInWithEmailAndPassword() {
        viewModelScope.launch {
            _state.mutate { copy(isLoading = true) }
            try {
                val email = state.value.email ?: return@launch
                val password = state.value.password ?: return@launch
                if(signIn(email, password).isSuccess) {
                    _event.trySend(SignInViewEvent.UserLoggedIn)
                }
            } catch (e: Exception) {
                _event.trySend(SignInViewEvent.Error(AuthenticationException().message))
            }
            _state.mutate { copy(isLoading = false) }
        }
    }

    private fun signInWithAuthCredential(credential: AuthCredential) {
        viewModelScope.launch {
            _state.mutate { copy(isLoading = true) }
            try {
                if(signInWithCredential(credential).isSuccess) {
                    _event.trySend(SignInViewEvent.UserLoggedIn)
                }
            } catch (e: Exception) {
                _event.trySend(SignInViewEvent.Error(AuthenticationException().message))
            }
            _state.mutate { copy(isLoading = false) }
        }
    }
}
