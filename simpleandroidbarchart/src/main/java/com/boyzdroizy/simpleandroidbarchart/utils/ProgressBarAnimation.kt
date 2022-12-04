package com.boyzdroizy.simpleandroidbarchart.utils

import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ProgressBar
import kotlin.math.abs

private const val MULTIPLIER = 100
private const val ANIMATION_DURATION: Long = 1500

class ProgressBarAnimation(
    private val progressBar: ProgressBar,
    progressTo: Int,
    maxValue: Int,
) : Animation() {
    private var valueFrom: Int = 0
    private var valueTo: Int = progressTo * MULTIPLIER
    private val mStepDuration: Long

    init {
        progressBar.max = maxValue * MULTIPLIER
        mStepDuration = ANIMATION_DURATION / progressBar.max
        this.duration = abs(valueTo - valueFrom) * mStepDuration

        progressBar.startAnimation(this)
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val value = valueFrom + (valueTo - valueFrom) * interpolatedTime
        progressBar.progress = value.toInt()
    }

}

