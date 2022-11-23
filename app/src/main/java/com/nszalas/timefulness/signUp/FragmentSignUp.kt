package com.nszalas.timefulness.signUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nszalas.timefulness.R
import com.nszalas.timefulness.databinding.FragmentSignUpBinding
import com.nszalas.timefulness.utils.showToast

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
            when {
                !validateEmail(email) -> {
                    showToast("Adres email nie jest poprawny!")
                }
                !validatePassword(password, confirmPassword) -> {
                    showToast("Sprawdź poprawność hasła!")
                }
                else -> {
                    viewModel.createUserWithEmailAndPassword(email, password) {
                        if (it.isSuccessful) {
                            showToast("Użytkownik utworzony pomyślnie!")
                            findNavController().navigate(FragmentSignUpDirections.actionFragmentSignUpToFragmentSignIn())
                        } else {
                            showToast("Coś poszło nie tak")
                        }
                    }
                }
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        return email.isNotBlank()
    }

    private fun validatePassword(password: String, confirmPassword: String): Boolean {
        return password.isNotBlank() && confirmPassword.isNotBlank() && password == confirmPassword
    }
}