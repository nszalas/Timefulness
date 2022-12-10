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
import java.time.LocalDate
import java.time.LocalTime

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

//    private fun addTask() {
//        val userId = FirebaseAuth.getInstance().currentUser!!.uid
//        val description = binding.getDescription.text.toString()
//        val date = binding.getDate.text.toString()
//        val time = binding.getTime.text.toString()
//        val category = binding.getCategory.text.toString()
//        // val repeat = binding.getRepeat.text.toString()
//
//        val task = Task(0, userId, description, date, time, category, false, false)
//        viewModel.insert(task)
//        Toast.makeText(requireContext(),"Dodano pomyślnie", Toast.LENGTH_LONG).show()
//        findNavController().navigate(R.id.action_addTaskFragment_to_navigation_today)
//    }
}