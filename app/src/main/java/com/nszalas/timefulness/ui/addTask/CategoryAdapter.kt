package com.nszalas.timefulness.ui.addTask

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.android.material.textview.MaterialTextView
import com.nszalas.timefulness.R
import com.nszalas.timefulness.ui.model.CategoryUI

class CategoryAdapter(
    context: Context,
    items: List<CategoryUI>
) : ArrayAdapter<CategoryUI>(
    context,
    0,
    items
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.add_task_category_item, parent, false)
        val categoryUI = getItem(position) ?: return view

        view.findViewById<MaterialTextView>(R.id.categoryImage).text = categoryUI.emoji
        view.findViewById<MaterialTextView>(R.id.categoryName).text = categoryUI.name
        return view
    }
}