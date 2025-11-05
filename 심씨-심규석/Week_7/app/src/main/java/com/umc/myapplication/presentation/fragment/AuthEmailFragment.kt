package com.umc.myapplication.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.umc.myapplication.R
import com.umc.myapplication.databinding.FragmentAuthEmailBinding
import com.umc.myapplication.presentation.utils.setUnderlinedSpannable
import com.umc.myapplication.presentation.feature.AuthViewModel
import com.umc.myapplication.presentation.feature.isValidEmail

class AuthEmailFragment : Fragment() {
    private val viewModel by activityViewModels<AuthViewModel>()
    private var _binding: FragmentAuthEmailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthEmailBinding.inflate(inflater, container, false)

        setUnderlinedSpannable(
            textView = binding.locationText,
            fullText = "대한민국 변경",
            underlineTargets = listOf("변경"),
            textColor = R.color.gray600
        )
        setUnderlinedSpannable(
            textView = binding.privacyPolicySummaryText,
            fullText = "계속 진행하면 나이키의  개인정보 처리방침 및 이용악관에 동의하게 됩니다.",
            underlineTargets = listOf("개인정보 처리방침 및 이용악관"),
            textColor = R.color.gray600
        )
        binding.emailInputLayout.editText?.doOnTextChanged { text, start, before, count ->
            val email = text.toString()
            if(email.isNotBlank() && isValidEmail(email)){
                viewModel.updateEmail(email)
                viewModel.setButtonEnabled(true)
            }
            else{
                viewModel.setButtonEnabled(false)
            }
        }
        return binding.root
    }

}