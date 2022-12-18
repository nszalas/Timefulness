package com.nszalas.timefulness.ui.taskDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nszalas.timefulness.databinding.DialogTaskDetailsBinding
import com.nszalas.timefulness.extensions.collectOnViewLifecycle
import com.nszalas.timefulness.ui.model.TaskWithCategoryUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskDetailsDialogFragment : DialogFragment() {

    private var _binding: DialogTaskDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<TaskDetailsViewModel>()

    override fun onStart() {
        super.onStart()

        dialog?.apply {
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogTaskDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectOnViewLifecycle(viewModel.event, ::onNewEvent)
        collectOnViewLifecycle(viewModel.state, ::onNewState)

        setupViews()

        val task = arguments?.get("task") as TaskWithCategoryUI?
        task?.let { viewModel.onTaskEditing(it) }
    }

    private fun onNewEvent(event: TaskDetailsViewEvent) {
        when (event) {
            TaskDetailsViewEvent.DeleteTask -> dismissDialog()
            TaskDetailsViewEvent.EditTask -> navigateToEditTask()
        }
    }

    private fun onNewState(state: TaskDetailsViewState) {
        with(binding) {
            taskTitle.text = state.title
            dateValueTextView.text = state.date
            startTimeValueTextView.text = state.startTime
            endTimeValueTextView.text = state.endTime
            categoryValueTextView.text = state.category
            completedCheckbox.isChecked = state.completed
        }
    }

    private fun setupViews() {
        with(binding) {
            completedCheckbox.setOnClickListener {
                viewModel.onCompletedChecked(completedCheckbox.isChecked)
            }
            deleteTaskButton.setOnClickListener { viewModel.onDeleteClicked() }
            editTaskButton.setOnClickListener { viewModel.onEditClicked() }
        }
    }

    private fun dismissDialog() = dialog?.dismiss()

    private fun navigateToEditTask() {
        findNavController().navigate(
            TaskDetailsDialogFragmentDirections.actionTaskDetailsDialogFragmentToAddTaskFragment(
                viewModel.state.value.taskWithCategory
            )
        )
    }
}