package com.nszalas.timefulness.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nszalas.timefulness.R
import com.nszalas.timefulness.databinding.FragmentProfileBinding
import com.nszalas.timefulness.extensions.collectOnViewLifecycle
import com.nszalas.timefulness.extensions.loadImage
import com.nszalas.timefulness.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.profile_today_tasks_card.*

@AndroidEntryPoint
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

        collectOnViewLifecycle(viewModel.state, ::onNewState)
        collectOnViewLifecycle(viewModel.event, ::onNewEvent)
        viewModel.onRefresh()

        setupViews()
    }

    private fun setupViews() {
        with(binding) {
            logoutButton.setOnClickListener { viewModel.onLogout() }
        }
    }

    private fun onNewState(state: ProfileViewState) {
        with(binding.profileUserCard) {
            state.user?.let { user ->
                welcomeTextView.text = getString(R.string.profile_welcome_message, user.name)
                emailTextView.text = user.email
                profileImage.loadImage(user.photoUrl)
            }
        }

        with(binding.profileStatisticsCard) {
            root.isVisible = state.statistics.isNotEmpty()
            tasksCompletedCountTextView.text = state.taskCompletedCount.toString()
            allTasksCountTextView.text = state.taskAllCount.toString()

            percentageView.text =
                getString(R.string.profile_task_completion_percentage, state.percentage)
            progressCircular.progress = state.percentage
            simpleBarChart.setChartData(state.statistics)
        }
    }

    private fun onNewEvent(event: ProfileViewEvent) {
        when(event) {
            is ProfileViewEvent.Error -> event.message?.let { requireContext().showToast(it) }
            ProfileViewEvent.UserLoggedOut -> findNavController().popBackStack(R.id.fragmentStart, inclusive = false)
        }
    }

}