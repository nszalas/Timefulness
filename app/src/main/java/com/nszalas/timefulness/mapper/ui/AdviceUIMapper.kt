package com.nszalas.timefulness.mapper.ui

import com.nszalas.timefulness.domain.model.Advice
import com.nszalas.timefulness.ui.model.AdviceUI

class AdviceUIMapper {
    operator fun invoke(advice: Advice): AdviceUI = with(advice) {
        AdviceUI(description = description)
    }
}