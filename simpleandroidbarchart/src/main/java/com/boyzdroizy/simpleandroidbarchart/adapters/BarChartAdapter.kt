package com.boyzdroizy.simpleandroidbarchart.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.boyzdroizy.simpleandroidbarchart.R
import com.boyzdroizy.simpleandroidbarchart.model.ChartData
import com.boyzdroizy.simpleandroidbarchart.utils.ProgressBarAnimation

class BarChartAdapter(
    private val items: List<ChartData>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.adapter_bar_chart,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(items[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val maxValue = items.maxOf { it.maxValue }
        private val paddingPerUnit =
            itemView.resources.getDimension(R.dimen.chart_height) / maxValue

        fun bind(item: ChartData) {
            val paddingValue = (maxValue - item.maxValue) * paddingPerUnit.toInt()
            itemView.let { view ->
                view.findViewById<TextView>(R.id.chart_value).text = item.label
                initProgressBar(
                    progressBar = view.findViewById(R.id.progressBar),
                    value = item.value,
                    maxValue = item.maxValue,
                )
                view.findViewById<ProgressBar>(R.id.progressBar).setPadding(0, paddingValue, 0, 0)
            }
        }

        private fun initProgressBar(progressBar: ProgressBar, value: Int, maxValue: Int) {
            ProgressBarAnimation(
                progressBar = progressBar,
                progressTo = value,
                maxValue = maxValue
            )
        }
    }
}
