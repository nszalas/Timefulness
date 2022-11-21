package com.nszalas.timefulness

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

class FragmentSignUp : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        firebaseAuth = FirebaseAuth.getInstance()

        view.findViewById<Button>(R.id.signUpButton).setOnClickListener {
            val email = view.findViewById<TextInputEditText>(R.id.newEmail).text.toString()
            val password = view.findViewById<TextInputEditText>(R.id.newPassword).text.toString()
            val confirmPassword = view.findViewById<TextInputEditText>(R.id.confirmPassword).text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
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

        return view
    }

}