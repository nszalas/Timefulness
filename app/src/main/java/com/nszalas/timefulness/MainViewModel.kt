package com.nszalas.timefulness

import androidx.lifecycle.ViewModel
import com.nszalas.timefulness.domain.usecase.IsUserLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val isUserLoggedIn: IsUserLoggedInUseCase
) : ViewModel() {
    fun isLoggedIn(): Boolean = isUserLoggedIn()
}