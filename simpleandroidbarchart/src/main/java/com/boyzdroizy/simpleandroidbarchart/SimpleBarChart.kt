package com.boyzdroizy.simpleandroidbarchart

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.boyzdroizy.simpleandroidbarchart.adapters.BarChartAdapter
import com.boyzdroizy.simpleandroidbarchart.model.ChartData


class SimpleBarChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val chartRecycler by lazy { findViewById<RecyclerView>(R.id.chart_recycler) }
    private val maxValueTextView by lazy { findViewById<TextView>(R.id.max_value) }
    private val minValueTextView by lazy { findViewById<TextView>(R.id.min_value) }
    private val midValueTextView by lazy { findViewById<TextView>(R.id.mid_value) }

    private fun initView(context: Context) {
        View.inflate(context, R.layout.bar_chart, this)
    }


    private fun initAnalyticsChartAdapter(items: List<ChartData>) {
        if (items.isEmpty()) return

        val maxValue = items.maxOf { it.maxValue }
        val minValue = 0
        changeMaxValueLabel(maxValue)
        changeMidValueLabel((maxValue + minValue) / 2f)
        changeMinValueLabel(minValue)
        val classesAdapter = BarChartAdapter(items)
        chartRecycler.layoutManager = GridLayoutManager(this.context, items.size)
        chartRecycler.adapter = classesAdapter
    }

    init {
        initView(context)
    }

    fun setChartData(chartData: List<ChartData>) {
        initAnalyticsChartAdapter(chartData)
    }

    private fun changeMaxValueLabel(maxValue: Int) {
        maxValueTextView.text = maxValue.toString()
    }

    private fun changeMidValueLabel(midValue: Float) {
        midValueTextView.text = if(midValue.isInteger()) {
            midValue.toInt().toString()
        } else {
            midValue.toString()
        }
    }

    @Suppress("SameParameterValue")
    private fun changeMinValueLabel(minValue: Int) {
        minValueTextView.text = minValue.toString()
    }

    private fun Float.isInteger(): Boolean = this.compareTo(this.toInt()) == 0
}
