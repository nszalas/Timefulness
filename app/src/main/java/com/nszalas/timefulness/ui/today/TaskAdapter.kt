package com.nszalas.timefulness.ui.today

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.nszalas.timefulness.R
import com.nszalas.timefulness.model.Task
import kotlinx.android.synthetic.main.task_row.view.*

class TaskAdapter: RecyclerView.Adapter<TaskAdapter.MyViewHolder>() {
    private var taskList = emptyList<Task>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val row = layoutInflater.inflate(R.layout.task_row, parent, false)
        return MyViewHolder(row)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = taskList[position]
        holder.itemView.taskTextView.text=currentItem.description
        holder.itemView.timeTextView.text=currentItem.time
        holder.itemView.categoryTextView.text=currentItem.category

    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun setData(task: List<Task>){
        this.taskList = task
        notifyDataSetChanged()
    }

}