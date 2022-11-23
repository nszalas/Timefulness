package com.nszalas.timefulness.signUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.nszalas.timefulness.R
import com.nszalas.timefulness.databinding.FragmentSignUpBinding

class FragmentSignUp : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.signUpButton.setOnClickListener {
            val email = binding.newEmail.text.toString()
            val password = binding.newEmail.text.toString()
            val confirmPassword = binding.newPassword.text.toString()

            if (validateEmail(email) && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            view.findNavController().navigate(R.id.action_fragmentSignUp_to_fragmentSignIn)
                        }
                        else {
                            Toast.makeText(activity, "Coś poszło nie tak", Toast.LENGTH_SHORT).show()
                        }
                    }


                } else {
                    Toast.makeText(activity, "Hasła są różne", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(activity, "Uzupełnij wszystkie pola", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        return email.isNotBlank()
    }

    private fun validatePassword(password: String, confirmPassword: String? = null): Boolean {

    }
}