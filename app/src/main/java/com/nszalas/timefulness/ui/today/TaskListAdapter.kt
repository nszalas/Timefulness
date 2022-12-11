package com.nszalas.timefulness.ui.today

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nszalas.timefulness.databinding.TodayTaskItemBinding
import com.nszalas.timefulness.extensions.asLocalDateTime
import com.nszalas.timefulness.ui.model.TaskWithCategoryUI
import com.nszalas.timefulness.utils.TimeFormatter
import java.time.LocalDateTime

class TaskListAdapter(
    private val taskActions: TaskListActions
) : ListAdapter<TaskWithCategoryUI, TaskListAdapter.TaskViewHolder>(
    NewsFeedDiffCallback()
) {
    class TaskViewHolder(
        private val binding: TodayTaskItemBinding,
        private val taskActions: TaskListActions,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val timeFormatter = TimeFormatter()

        fun bind(taskWithCategory: TaskWithCategoryUI) {
            val startDateTime: LocalDateTime = taskWithCategory.task.let { task ->
                task.startTimestamp.asLocalDateTime(task.timezoneId)
            }
            val startTime: String? = timeFormatter.formatTime(startDateTime.toLocalTime())

            with(binding) {
                // click actions
                root.setOnClickListener {
                    taskActions.onItemClicked(position = adapterPosition)
                }
                taskCheckbox.setOnClickListener {
                    taskActions.onItemChecked(position = adapterPosition, checked = taskCheckbox.isChecked)
                }

                // task info
                taskCheckbox.isChecked = taskWithCategory.task.completed
                taskTitle.paint.isStrikeThruText = taskWithCategory.task.completed
                taskTitle.text = taskWithCategory.task.title
                this.startTime.text = startTime

                // category info
                categoryLabel.text = taskWithCategory.category.name
                categoryIconLabel.text = taskWithCategory.category.emoji
                this.startTime.setBackgroundColor(taskWithCategory.category.colorMain)
                this.startTime.setTextColor(taskWithCategory.category.colorText)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TodayTaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding, taskActions)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface TaskListActions {
        fun onItemClicked(position: Int)
        fun onItemChecked(position: Int, checked: Boolean)
    }

}

class NewsFeedDiffCallback : DiffUtil.ItemCallback<TaskWithCategoryUI>() {
    override fun areItemsTheSame(
        oldItem: TaskWithCategoryUI,
        newItem: TaskWithCategoryUI
    ) = oldItem.task.id == newItem.task.id

    override fun areContentsTheSame(
        oldItem: TaskWithCategoryUI,
        newItem: TaskWithCategoryUI
    ) = when {
        oldItem.task != newItem.task -> false
        oldItem.category != newItem.category -> false
        else -> true
    }
}