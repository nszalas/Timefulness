package com.nszalas.timefulness.ui.calendar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.app.ActivityCompat
import com.nszalas.timefulness.R
import com.nszalas.timefulness.ui.calendar.utils.getWeeklyViewItemHeight

private const val ROW_COUNT = 24
private const val DAY_COUNT = 7

class WeeklyViewGrid(context: Context, attrs: AttributeSet, defStyle: Int) : View(context, attrs, defStyle) {
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    init {
        paint.color = ActivityCompat.getColor(context, R.color.divider_grey)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val rowHeight = context.getWeeklyViewItemHeight()
        for (i in 0 until ROW_COUNT) {
            val y = rowHeight * i.toFloat()
            canvas.drawLine(0f, y, width.toFloat(), y, paint)
        }

        val rowWidth = width / DAY_COUNT.toFloat()
        for (i in 0 until DAY_COUNT) {
            val x = rowWidth * i.toFloat()
            canvas.drawLine(x, 0f, x, height.toFloat(), paint)
        }
    }
}
