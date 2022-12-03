package com.nszalas.timefulness.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.nszalas.timefulness.R
import com.nszalas.timefulness.databinding.FragmentProfileBinding
import kotlinx.android.synthetic.main.profile_today_tasks_card.*

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        viewModel.loadTodayTaskCompletion()
        viewModel.loadStatistics()
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { onNewState(it) }
    }

    private fun onNewState(state: ProfileViewState) {
        with(binding.profileStatisticsCard) {
            tasksCompletedCountTextView.text = state.taskCompletedCount.toString()
            allTasksCountTextView.text = state.taskAllCount.toString()

            percentageView.text = getString(R.string.profile_task_completion_percentage, state.percentage)
            progressCircular.progress = state.percentage
            simpleBarChart.setChartData(state.statistics)
        }
    }

}