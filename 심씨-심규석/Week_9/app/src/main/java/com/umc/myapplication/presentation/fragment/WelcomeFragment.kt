package com.umc.myapplication.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.umc.myapplication.databinding.FragmentWelcomeBinding
import com.umc.myapplication.presentation.feature.AuthViewModel
import com.umc.myapplication.util.kakaoLogin

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel : AuthViewModel by activityViewModels()
    private val TAG = "WelcomeFragment"
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
        binding.kakaoLogin.setOnClickListener {
            kakaoLogin(requireContext(),
                onSuccess = {
                    viewModel.signInWithKakao(it)
                }
            , onError = {
                    Log.e(TAG, "onCreateView: ${it?.message}", )
                })
        }
        return binding.root
    }
}