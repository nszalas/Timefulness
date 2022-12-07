package com.nszalas.timefulness.ui.addTask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nszalas.timefulness.databinding.FragmentAddTaskBinding
import com.nszalas.timefulness.extensions.collectOnViewLifecycle

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
    }

    private fun onNewEvent(event: AddTaskViewEvent) {
        if(event is AddTaskViewEvent.TaskAdded) {
            findNavController().navigateUp()
        }
    }

    private fun onNewState(state: AddTaskViewState) {

    }

    private fun setupButtons() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.addTaskButton.setOnClickListener {
            viewModel.addTask()
        }
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