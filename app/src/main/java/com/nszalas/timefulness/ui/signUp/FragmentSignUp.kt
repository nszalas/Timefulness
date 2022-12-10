package com.nszalas.timefulness.ui.signUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nszalas.timefulness.R
import com.nszalas.timefulness.databinding.FragmentSignUpBinding
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

        binding.signUpButton.setOnClickListener { signUp() }
    }

    private fun signUp() {
        val email = binding.newEmail.text.toString()
        val password = binding.newPassword.text.toString()
        val confirmPassword = binding.confirmPassword.text.toString()

        with(requireContext()) {
            viewModel.createUserWithEmailAndPassword(email, password, confirmPassword) {
                if (it.isSuccess) {
                    showToast(R.string.register_user_created_success)
                    findNavController().navigate(FragmentSignUpDirections.actionFragmentSignUpToNavigationCalendar())
                } else {
                    it.exceptionOrNull()?.message?.let { message ->
                        showToast(message)
                    }
                }
            }
        }
    }
}