package com.nszalas.timefulness.ui.other

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nszalas.timefulness.domain.usecase.GetAdviceForTodayUseCase
import com.nszalas.timefulness.domain.usecase.GetTechniqueForTodayUseCase
import com.nszalas.timefulness.extensions.mutate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class OtherViewModel @Inject constructor(
    private val getTechniqueForToday: GetTechniqueForTodayUseCase,
    private val getAdviceForToday: GetAdviceForTodayUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(OtherViewState())
    val state: StateFlow<OtherViewState> = _state.asStateFlow()

    private var timer: CountDownTimer? = null

    init {
        initializePomodoro()
    }

    fun onRefresh() {
        loadAdviceForToday()
        loadTechniqueForToday()
    }

    fun onLogOut() {
        viewModelScope.launch {
            timer?.cancel()
            onTimerStatusChanged(PomodoroStatus.Regular())
        }
    }

    fun onTimerStatusChanged(status: PomodoroStatus) {
        val time: Long = TimeUnit.MINUTES.toSeconds(status.minutes)
        timer?.cancel()
        viewModelScope.launch {
            _state.mutate {
                copy(timeLeft = time, pomodoroStatus = status)
            }
        }
    }

    fun startCountdown() {
        timer?.cancel()
        viewModelScope.launch {
            val millis = TimeUnit.SECONDS.toMillis(state.value.timeLeft)

            _state.mutate {
                copy(pomodoroStatus = PomodoroStatus.CountingDown())
            }

            timer = object : CountDownTimer(millis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    _state.mutate {
                        copy(timeLeft = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished))
                    }
                }

                override fun onFinish() {
                    _state.mutate {
                        copy(pomodoroStatus = PomodoroStatus.Stopped())
                    }
                }
            }.start()
        }
    }

    private fun initializePomodoro() {
        val time: Long = TimeUnit.MINUTES.toSeconds(25)
        viewModelScope.launch {
            _state.mutate {
                copy(timeLeft = time)
            }
        }
    }

    private fun loadTechniqueForToday() {
        viewModelScope.launch {
            val technique = withContext(IO) { getTechniqueForToday() }

            _state.mutate {
                copy(
                    technique = technique
                )
            }
        }
    }

    private fun loadAdviceForToday() {
        viewModelScope.launch {
            val advice = withContext(IO) { getAdviceForToday() }

            _state.mutate {
                copy(
                    advice = advice,
                )
            }
        }
    }
}