package com.nszalas.timefulness.ui.today

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.nszalas.timefulness.R
import com.nszalas.timefulness.extensions.asLocalDateTime
import com.nszalas.timefulness.infrastructure.local.entity.TaskWithCategoryEntity
import kotlinx.android.synthetic.main.task_row.view.*
import java.time.ZoneId

class TaskAdapter: RecyclerView.Adapter<TaskAdapter.MyViewHolder>() {
    private var taskList = emptyList<TaskWithCategoryEntity>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val row = layoutInflater.inflate(R.layout.task_row, parent, false)
        return MyViewHolder(row)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = taskList[position]
        holder.itemView.taskTextView.text = currentItem.task.title
        holder.itemView.timeTextView.text = currentItem.task.startTimestamp.asLocalDateTime(ZoneId.systemDefault().id).toString()
        holder.itemView.categoryTextView.text = currentItem.category.name

        holder.itemView.taskRow.setOnClickListener {
            holder.itemView.findNavController().navigate(TodayFragmentDirections.actionNavigationTodayToAddTaskFragment())
        }

        holder.itemView.taskTextView.text = currentItem.task.title
        holder.itemView.timeTextView.text = currentItem.task.startTimestamp.asLocalDateTime(ZoneId.systemDefault().id).toString()
        holder.itemView.categoryTextView.text = currentItem.category.name
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun setData(tasks: List<TaskWithCategoryEntity>){
        this.taskList = tasks
        notifyDataSetChanged()
    }

}