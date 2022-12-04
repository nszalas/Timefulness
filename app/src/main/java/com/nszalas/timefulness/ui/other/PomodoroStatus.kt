package com.nszalas.timefulness.ui.other

sealed interface PomodoroStatus {
    val minutes: Long

    data class CountingDown(
        override val minutes: Long = -1
    ) : PomodoroStatus

    data class Stopped(
        override val minutes: Long = 0
    ) : PomodoroStatus

    data class Regular(
        override val minutes: Long = 25
    ) : PomodoroStatus

    data class LongBreak(
        override val minutes: Long = 15
    ) : PomodoroStatus

    data class ShortBreak(
        override val minutes: Long = 5
    ) : PomodoroStatus
}
