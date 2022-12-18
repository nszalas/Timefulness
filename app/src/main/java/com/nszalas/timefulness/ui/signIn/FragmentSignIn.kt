package com.nszalas.timefulness.ui.signIn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nszalas.timefulness.databinding.FragmentSignInBinding
import com.nszalas.timefulness.extensions.collectOnViewLifecycle
import com.nszalas.timefulness.extensions.setup
import com.nszalas.timefulness.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSignIn : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SignInViewModel>()

    private val launcher = registerForActivityResult(StartActivityForResult()) { result ->
        viewModel.signInWithCredentialFromActivityResult(result)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectOnViewLifecycle(viewModel.state, ::onNewState)
        collectOnViewLifecycle(viewModel.event, ::onNewEvent)

        setupViews()
    }

    private fun setupViews() {
        binding.signInButton.setOnClickListener { viewModel.onSignIn() }
        binding.signInGoogle.setOnClickListener { viewModel.onGoogleSignIn() }
        binding.email.setup(viewModel::onEmailEntered)
        binding.password.setup(viewModel::onPasswordEntered)
    }

    private fun onNewState(state: SignInViewState) {
        with(binding) {
            signInButton.isEnabled = state.buttonEnabled
            progressLayout.isVisible = state.isLoading

            email.error = state.emailError
            password.error = state.passwordError
        }
    }

    private fun onNewEvent(event: SignInViewEvent) {
        when (event) {
            is SignInViewEvent.Error -> event.message?.let { requireContext().showToast(it) }
            SignInViewEvent.UserLoggedIn -> findNavController().navigate(FragmentSignInDirections.actionFragmentSignInToNavigationCalendar())
            is SignInViewEvent.LaunchActivityWithIntent -> launcher.launch(event.intent)
        }
    }
}