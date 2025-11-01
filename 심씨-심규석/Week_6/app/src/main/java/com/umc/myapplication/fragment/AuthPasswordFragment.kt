package com.umc.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.umc.myapplication.R
import com.umc.myapplication.databinding.FragmentAuthEmailBinding
import com.umc.myapplication.databinding.FragmentAuthPasswordBinding
import com.umc.myapplication.utils.setUnderlinedSpannable
import com.umc.myapplication.viewmodel.AuthViewModel
import com.umc.myapplication.viewmodel.isValidEmail
import kotlin.getValue

class AuthPasswordFragment : Fragment() {

    private val viewModel by activityViewModels<AuthViewModel>()
    private var _binding: FragmentAuthPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthPasswordBinding.inflate(inflater, container, false)

        binding.email.text = viewModel.form.value?.email
        setUnderlinedSpannable(
            textView = binding.emailEditButton,
            fullText = "수정",
            underlineTargets = listOf("수정"),
        )
        setUnderlinedSpannable(
            textView = binding.forgetPasswordButton,
            fullText = "패스워드를 잊으셨나요?",
            underlineTargets = listOf("패스워드를 잊으셨나요?"),
        )

        //그냥 진행할 것
        binding.passwordInputLayout.editText?.doOnTextChanged { text, start, before, count ->
            val password = text.toString()
            if (password.isNotBlank()) {
                viewModel.setButtonEnabled(true)
            } else {
                viewModel.setButtonEnabled(false)
            }
        }



        return binding.root
    }
}