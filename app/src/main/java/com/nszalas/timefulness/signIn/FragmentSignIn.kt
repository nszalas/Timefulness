package com.nszalas.timefulness.signIn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nszalas.timefulness.databinding.FragmentSignInBinding
import com.nszalas.timefulness.utils.showToast

class FragmentSignIn : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SignInViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInButton.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        viewModel.signInWithEmailAndPassword(email, password) {
            if (it.isSuccess) {
                findNavController().navigate(FragmentSignInDirections.actionFragmentSignInToFragmentHome())
            } else {
                it.exceptionOrNull()?.message?.let { message -> requireContext().showToast(message) }
            }
        }
    }
}