package com.umc.myapplication.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.umc.myapplication.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        binding.signInButton.setOnClickListener {
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToSignInFragment()
            findNavController().navigate(action)
        }
        binding.signUpButton.setOnClickListener {
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToSignUpFragment()
            findNavController().navigate(action)
        }
        return binding.root
    }
}