package com.nszalas.timefulness.ui.signIn

data class SignInViewState(
    val isLoading: Boolean = false,
    val email: String? = null,
    val emailError: String? = null,
    val password: String? = null,
    val passwordError: String? = null,
    val buttonEnabled: Boolean = false,
)

sealed class SignInViewEvent {
    object UserLoggedIn : SignInViewEvent()
    data class Error(val message: String?) : SignInViewEvent()
}