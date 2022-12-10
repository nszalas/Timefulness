package com.nszalas.timefulness.ui.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.nszalas.timefulness.databinding.FragmentStartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentStart : Fragment() {
    private var _binding: FragmentStartBinding? = null
    private val binding: FragmentStartBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
    }

    private fun setupButtons() {
        with(binding) {
            loginButton.setOnClickListener {
                findNavController().navigate(FragmentStartDirections.actionFragmentStartToFragmentSignIn())
            }
            registerButton.setOnClickListener {
                findNavController().navigate(FragmentStartDirections.actionFragmentStartToFragmentSignUp())
            }
        }
    }

}