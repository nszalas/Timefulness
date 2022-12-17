package com.nszalas.timefulness.ui.signUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nszalas.timefulness.databinding.FragmentSignUpBinding
import com.nszalas.timefulness.extensions.collectOnViewLifecycle
import com.nszalas.timefulness.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSignUp : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SignUpViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectOnViewLifecycle(viewModel.event, ::onNewEvent)
        collectOnViewLifecycle(viewModel.state, ::onNewState)
        setupViews()
    }

    private fun setupViews() {
        binding.signUpButton.setOnClickListener { signUp() }
    }

    private fun onNewState(state: SignUpViewState) {
        binding.progressLayout.isVisible = state.isLoading
    }

    private fun onNewEvent(event: SignUpViewEvent) {
        when (event) {
            is SignUpViewEvent.Error -> event.message?.let { requireContext().showToast(it) }
            SignUpViewEvent.SignUpSuccess -> findNavController().navigate(
                FragmentSignUpDirections.actionFragmentSignUpToNavigationCalendar()
            )
        }
    }

    private fun signUp() {
        val name = binding.newUsername.text.toString()
        val email = binding.newEmail.text.toString()
        val password = binding.newPassword.text.toString()
        val confirmPassword = binding.confirmPassword.text.toString()

        viewModel.createUser(
            name = name,
            email = email,
            password = password,
            confirmPassword = confirmPassword
        )
    }
}