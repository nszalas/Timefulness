package com.nszalas.timefulness

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController

class FragmentStart : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start, container, false)

        view.findViewById<Button>(R.id.logIn).setOnClickListener {
            view.findNavController().navigate(R.id.action_fragmentStart_to_fragmentSignIn)
        }

        view.findViewById<Button>(R.id.register).setOnClickListener {
            view.findNavController().navigate(R.id.action_fragmentStart_to_fragmentSignUp)
        }

        return view
    }

}