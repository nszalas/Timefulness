package com.nszalas.timefulness

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class FragmentSignIn : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        firebaseAuth = FirebaseAuth.getInstance()

        view.findViewById<Button>(R.id.signInButton).setOnClickListener {
            val email = view.findViewById<TextInputEditText>(R.id.email).text.toString()
            val password = view.findViewById<TextInputEditText>(R.id.password).text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        view.findNavController().navigate(R.id.action_fragmentSignIn_to_fragmentHome)
                    }
                    else {
                        Toast.makeText(activity, "Coś poszło nie tak", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(activity, "Uzupełnij wszystkie pola", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

}