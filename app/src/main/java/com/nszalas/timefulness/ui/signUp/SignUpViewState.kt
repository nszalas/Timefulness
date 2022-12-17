package com.nszalas.timefulness.ui.signUp

data class SignUpViewState(
    val isLoading: Boolean = false
)

sealed class SignUpViewEvent {
    object SignUpSuccess : SignUpViewEvent()
    data class Error(val message: String?) : SignUpViewEvent()
}