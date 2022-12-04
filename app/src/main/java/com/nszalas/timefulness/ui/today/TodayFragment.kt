package com.nszalas.timefulness.ui.today

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nszalas.timefulness.R
import com.nszalas.timefulness.databinding.FragmentTodayBinding
import com.nszalas.timefulness.ui.signUp.FragmentSignUpDirections
import com.nszalas.timefulness.utils.showToast
import kotlinx.android.synthetic.main.fragment_today.view.*

class TodayFragment : Fragment() {

    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TodayViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)

        // val view = inflater.inflate(R.layout.fragment_today, container, false)
        val adapter = TaskAdapter()
        val recyclerView = binding.viewTaskList
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this).get(TodayViewModel::class.java)
        viewModel.getAll.observe(viewLifecycleOwner, Observer { task ->
            adapter.setData(task)
        })

       binding.buttonAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_today_to_addTaskFragment)
        }

       return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.buttonAddTask.setOnClickListener { addTask() }
//    }
//
//    private fun addTask() {
//        findNavController().navigate(R.id.action_navigation_today_to_addTaskFragment)
//
//    }


}
