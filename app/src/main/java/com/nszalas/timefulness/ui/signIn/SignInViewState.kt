package com.nszalas.timefulness.ui.signIn

import android.content.Intent
import com.google.firebase.auth.AuthCredential

data class SignInViewState(
    val isLoading: Boolean = false,
    val email: String? = null,
    val emailError: String? = null,
    val password: String? = null,
    val passwordError: String? = null,
    val buttonEnabled: Boolean = false,
    val credential: AuthCredential? = null,
)

sealed class SignInViewEvent {
    object UserLoggedIn : SignInViewEvent()
    data class Error(val message: String?) : SignInViewEvent()
    data class LaunchActivityWithIntent(val intent: Intent) : SignInViewEvent()
}