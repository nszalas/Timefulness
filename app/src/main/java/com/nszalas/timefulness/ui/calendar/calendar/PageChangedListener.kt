package com.nszalas.timefulness.ui.calendar.calendar

import androidx.viewpager.widget.ViewPager

class PageChangedListener(
    var currentWeekTS: Long,
    private val weekTSs: List<Long>,
    val setupWeeklyActionbarTitle: (Long) -> Unit,
): ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) {
    }

    override fun onPageSelected(position: Int) {
        currentWeekTS = weekTSs[position]
        setupWeeklyActionbarTitle(weekTSs[position])
    }
}