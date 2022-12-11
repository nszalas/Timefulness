package com.nszalas.timefulness.ui.addTask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nszalas.timefulness.databinding.FragmentAddTaskBinding
import com.nszalas.timefulness.extensions.collectOnViewLifecycle
import com.nszalas.timefulness.extensions.setup
import com.nszalas.timefulness.ui.model.CategoryUI
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalTime

@AndroidEntryPoint
class AddTaskFragment : Fragment() {

    private val viewModel: AddTaskViewModel by viewModels()

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

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
    }

    private fun setupViews() {
        with(binding) {
            taskName.setup(viewModel::onEventNameEntered)
            dateTextView.setOnClickListener { viewModel.onDateClicked() }
            startTimeTextView.setOnClickListener { viewModel.onStartTimeClicked() }
            endTimeTextView.setOnClickListener { viewModel.onEndTimeClicked() }
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
            AddTaskViewEvent.TaskAdded -> {
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

            categorySpinner.adapter ?: run { setupSpinner() }
        }
    }

    private fun setupButtons() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.addTaskButton.setOnClickListener {
            viewModel.onAddButtonClicked()
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
}