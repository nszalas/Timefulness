package com.nszalas.timefulness.ui.today

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nszalas.timefulness.databinding.FragmentTodayBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodayFragment : Fragment() {

    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TodayViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TaskAdapter()
        val recyclerView = binding.viewTaskList
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

//        viewModel.{ task ->
//            adapter.setData(task)
//        }

        binding.buttonAddTask.setOnClickListener {
            findNavController().navigate(TodayFragmentDirections.actionNavigationTodayToAddTaskFragment(null))
        }

        binding.buttonDeleteAll.setOnClickListener {
            deleteItems()
        }

    }

    private fun deleteItems(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Tak"){ _, _ ->
            viewModel.deleteAll()
            Toast.makeText(requireContext(),"Usunięto", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("Nie"){ _, _ ->}
        builder.setMessage("Czy na pewno chcesz wyczyścić listę zadań?")

        builder.create().show()



    }

}
