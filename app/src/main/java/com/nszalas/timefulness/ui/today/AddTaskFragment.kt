package com.nszalas.timefulness.ui.today

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.nszalas.timefulness.R
import com.nszalas.timefulness.databinding.FragmentAddTaskBinding
import com.nszalas.timefulness.model.Task

class AddTaskFragment : Fragment() {

    private val args by navArgs<AddTaskFragmentArgs>()
    private lateinit var viewModel: TodayViewModel

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(TodayViewModel::class.java)

        if (args.currentTask === null) {
            binding.addTaskButton.setOnClickListener { addTask() }
        } else {
            setData()
        }

    }

    private fun addTask() {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val description = binding.getDescription.text.toString()
        val date = binding.getDate.text.toString()
        val time = binding.getTime.text.toString()
        val category = binding.getCategory.text.toString()
        // val repeat

        val newTask = Task(0, userId, description, date, time, category, false, false)
        viewModel.insert(newTask)
        Toast.makeText(requireContext(),"Dodano pomyślnie", Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_addTaskFragment_to_navigation_today)
    }

    private fun setData() {
        binding.getDescription.setText(args.currentTask?.description)
        binding.getDate.setText(args.currentTask?.date)
        binding.getTime.setText(args.currentTask?.time)
        binding.getCategory.setText(args.currentTask?.category)

        binding.addTaskButton.setOnClickListener { editTask() }
    }

    private fun editTask() {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val description = binding.getDescription.text.toString()
        val date = binding.getDate.text.toString()
        val time = binding.getTime.text.toString()
        val category = binding.getCategory.text.toString()

        val task = Task(args.currentTask!!.task_id, userId, description, date, time, category, false, false)
        viewModel.update(task)
        Toast.makeText(requireContext(),"Edytowano pomyślnie", Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_addTaskFragment_to_navigation_today)
    }
}