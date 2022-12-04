package com.nszalas.timefulness.ui.other

import com.nszalas.timefulness.ui.model.AdviceUI
import com.nszalas.timefulness.ui.model.TechniqueUI
import com.nszalas.timefulness.ui.other.PomodoroStatus.Regular

data class OtherViewState(
    val advice: AdviceUI? = null,
    val technique: TechniqueUI? = null,
    val timeLeft: Long = 0,
    val pomodoroStatus: PomodoroStatus = Regular()
)