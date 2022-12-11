package com.nszalas.timefulness.ui.addTask

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nszalas.timefulness.R
import com.nszalas.timefulness.databinding.FragmentAddTaskBinding
import com.nszalas.timefulness.extensions.collectOnViewLifecycle
import com.nszalas.timefulness.extensions.setup
import com.nszalas.timefulness.ui.model.CategoryUI
import com.nszalas.timefulness.ui.model.TaskWithCategoryUI
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalTime

@AndroidEntryPoint
class AddTaskFragment : Fragment() {

    private val viewModel: AddTaskViewModel by viewModels()

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    private var editingDataInserted = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectOnViewLifecycle(viewModel.event, ::onNewEvent)
        collectOnViewLifecycle(viewModel.state, ::onNewState)

        setupButtons()
        setupViews()

        val task = arguments?.get("task") as TaskWithCategoryUI?
        task?.let { viewModel.onTaskEditing(it) }
    }

    private fun setupViews() {
        with(binding) {
            taskName.setup(viewModel::onEventNameEntered)
            dateTextView.setOnClickListener { viewModel.onDateClicked() }
            startTimeTextView.setOnClickListener { viewModel.onStartTimeClicked() }
            endTimeTextView.setOnClickListener { viewModel.onEndTimeClicked() }
        }
    }

    private fun insertEditingData() {
        if (editingDataInserted) return
        editingDataInserted = true

        with(binding) {
            taskName.setText(viewModel.state.value.taskTitle)
            categorySpinner.setSelection(viewModel.state.value.categoryId ?: 0)
        }
    }

    private fun setupSpinner() {
        val categories: List<CategoryUI> = viewModel.state.value.categories
        val adapter = CategoryAdapter(requireContext(), categories)
        binding.categorySpinner.apply {
            this.adapter = adapter
            onItemSelectedListener = CategoryItemSelectedListener { category ->
                viewModel.onCategoryPicked(category)
            }
        }
    }

    private fun onNewEvent(event: AddTaskViewEvent) {
        when (event) {
            AddTaskViewEvent.NavigateBack -> {
                findNavController().navigateUp()
            }
            is AddTaskViewEvent.OpenDatePicker -> {
                openDatePicker(event.localDate, viewModel::onDatePicked)
            }
            is AddTaskViewEvent.OpenTimePicker -> {
                openTimePicker(event.localTime, event.taskTimeType, viewModel::onTimePicked)
            }
        }
    }

    private fun onNewState(state: AddTaskViewState) {
        with(binding) {
            startTimeTextView.text = state.startTime
            endTimeTextView.text = state.endTime
            dateTextView.text = state.date

            taskName.error = state.taskTitleError?.let { getString(it) }

            addTaskButton.isEnabled = state.addButtonEnabled
            addTaskButton.text = getString(
                if (state.isEditing) { R.string.add_update } else { R.string.add_save }
            )

            deleteTaskButton.isVisible = state.isEditing

            categorySpinner.adapter ?: run { setupSpinner() }

            if (state.isEditing) { insertEditingData() }
        }
    }

    private fun setupButtons() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.addTaskButton.setOnClickListener {
            viewModel.onAddClicked()
        }

        binding.deleteTaskButton.setOnClickListener {
            showDeleteDialog()
        }
    }

    private fun openDatePicker(
        localDate: LocalDate,
        onDatePicked: (Int, Int, Int) -> Unit
    ) {
        DatePickerDialog(
            requireContext(),
            { _, year, month, day -> onDatePicked(year, month + 1, day) },
            localDate.year,
            localDate.monthValue - 1,
            localDate.dayOfMonth
        ).show()
    }

    private fun openTimePicker(
        localTime: LocalTime,
        taskTimeType: TaskTimeType,
        onTimePicked: (Int, Int, TaskTimeType) -> Unit
    ) {
        TimePickerDialog(
            requireContext(),
            { _, hours, minutes -> onTimePicked(hours, minutes, taskTimeType) },
            localTime.hour,
            localTime.minute,
            true
        ).show()
    }

    private fun showDeleteDialog(){
        AlertDialog.Builder(requireContext()).apply {
            setPositiveButton(getString(R.string.add_delete_rationale_positive)){ _, _ -> viewModel.onDeleteClicked() }
            setNegativeButton(R.string.add_delete_rationale_negative){ _, _ -> }
            setMessage(getString(R.string.add_delete_rationale))
            setCancelable(false)
            create()
            show()
        }
    }
}