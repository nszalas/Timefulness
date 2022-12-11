package com.nszalas.timefulness.ui.addTask

import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import com.nszalas.timefulness.ui.model.CategoryUI

class CategoryItemSelectedListener(
    val onSelected: (CategoryUI) -> Unit
): OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val clickedItem: CategoryUI = parent?.getItemAtPosition(position) as CategoryUI
        onSelected(clickedItem)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // no op
    }
}