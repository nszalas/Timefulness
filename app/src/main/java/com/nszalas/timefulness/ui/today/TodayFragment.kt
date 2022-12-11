package com.nszalas.timefulness.ui.today

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nszalas.timefulness.databinding.FragmentTodayBinding
import com.nszalas.timefulness.extensions.collectOnViewLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodayFragment : Fragment(), TaskListAdapter.TaskListActions {

    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TodayViewModel by viewModels()

    private val taskListAdapter by lazy { TaskListAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()

        collectOnViewLifecycle(viewModel.state, ::onNewState)
        collectOnViewLifecycle(viewModel.event, ::onNewEvent)
    }

    private fun onNewEvent(event: TodayViewEvent) {
        when(event) {
            TodayViewEvent.AddTaskClicked -> {
                findNavController().navigate(TodayFragmentDirections.actionNavigationTodayToAddTaskFragment())
            }
            is TodayViewEvent.TaskChecked -> {
                // no op
            }
            is TodayViewEvent.TaskClicked -> {
                val task = viewModel.state.value.tasks[event.id]
                findNavController().navigate(TodayFragmentDirections.actionNavigationTodayToAddTaskFragment(task = task))
            }
        }
    }

    private fun onNewState(state: TodayViewState) {
        taskListAdapter.submitList(state.tasks)
    }

    private fun setupViews() {
        with(binding) {
            buttonAddTask.setOnClickListener { viewModel.onAddTaskClicked() }

            taskList.apply {
                setHasFixedSize(true)
                adapter = taskListAdapter
            }
        }
    }

    // recyclerView onclick actions

    override fun onItemClicked(position: Int) {
        viewModel.onTaskClicked(position)
    }

    override fun onItemChecked(position: Int, checked: Boolean) {
        viewModel.onTaskChecked(position, checked)
    }

}
