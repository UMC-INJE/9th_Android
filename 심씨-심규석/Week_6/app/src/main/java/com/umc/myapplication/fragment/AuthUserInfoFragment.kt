package com.umc.myapplication.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.umc.myapplication.R
import com.umc.myapplication.databinding.FragmentAuthUserInfoBinding
import com.umc.myapplication.utils.setUnderlinedSpannable
import com.umc.myapplication.viewmodel.AuthViewModel
import com.umc.myapplication.viewmodel.containsUpperLowerAndDigit
import com.umc.myapplication.viewmodel.isValidBirth
import com.umc.myapplication.viewmodel.isValidEmail
import com.umc.myapplication.viewmodel.isValidLength

class AuthUserInfoFragment : Fragment() {

    private val viewModel by activityViewModels<AuthViewModel>()
    private var _binding: FragmentAuthUserInfoBinding? = null
    private val binding get() = _binding!!

    // 이 Fragment 전용 로컬 유효성 플래그

    private var localFirstNameValid = false
    private var localLastNameValid = false
    private var localBirthValid = false
    private var localPwLenValid = false
    private var localPwRuleValid = false

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthUserInfoBinding.inflate(inflater, container, false)
        setupPasswordRules()
        setupPrivacyPolicy()
        setupInputListeners()
        updateStartButtonEnabled() // 초기 상태 반영
        return binding.root
    }

    private fun setupPasswordRules() {
        with(binding) {
            passwordRuleMinLength.textView.text = "최소 8자"
            passwordRuleMixedCaseAndDigit.textView.text = "알파벳 대문자 및 소문자 조합, 최소 1개 이상의 숫자"
            birthRule.apply {
                icon.visibility = View.GONE
                textView.text = "필수"
            }
        }
    }

    private fun setupPrivacyPolicy() {
        setUnderlinedSpannable(
            textView = binding.privacyPolicySummaryCheckBox,
            fullText = "나이키의 개인정보 처리방침 및 이용약관에 동의합니다.",
            underlineTargets = listOf("개인정보 처리방침", "이용약관"),
            textColor = R.color.gray600
        )
    }

    private fun setupInputListeners() = with(binding) {

        passwordInputLayout.editText?.doOnTextChanged { text, _, _, _ ->
            val pw = text?.toString().orEmpty()
            handlePasswordInput(pw)
            viewModel.updatePassword(pw)
            localPwLenValid = isValidLength(pw)
            localPwRuleValid = containsUpperLowerAndDigit(pw)
            updateStartButtonEnabled()
        }

        birthInputLayout.editText?.doOnTextChanged { text, _, _, _ ->
            val b = text?.toString().orEmpty()
            handleBirthInput(b)
            // handleBirthInput에서 색상 반영은 하되 여기서도 로컬 플래그 유지
            localBirthValid = b.isNotBlank() && isValidBirth(b)
            viewModel.updateBirth(b)
            updateStartButtonEnabled()
        }

        firstNameLayout.editText?.doOnTextChanged { text, _, _, _ ->
            val s = text?.toString().orEmpty()
            viewModel.updateFirstName(s)
            localFirstNameValid = s.isNotBlank()
            updateStartButtonEnabled()
        }

        lastNameLayout.editText?.doOnTextChanged { text, _, _, _ ->
            val s = text?.toString().orEmpty()
            viewModel.updateLastName(s)
            localLastNameValid = s.isNotBlank()
            updateStartButtonEnabled()
        }
        privacyPolicySummaryCheckBox.setOnCheckedChangeListener { _, isChecked ->
            updateStartButtonEnabled()
        }
    }

    private fun handlePasswordInput(password: String) = with(binding) {
        if (!isValidLength(password)) {
            updatePasswordRuleView(passwordRuleMinLength, false)
        } else {
            updatePasswordRuleView(passwordRuleMinLength, true)
        }

        if (!containsUpperLowerAndDigit(password)) {
            updatePasswordRuleView(passwordRuleMixedCaseAndDigit, false)
        } else {
            updatePasswordRuleView(passwordRuleMixedCaseAndDigit, true)
        }
    }

    private fun handleBirthInput(date: String) = with(binding) {
        if (date.isNotBlank() && isValidBirth(date)) {
            birthRule.textView.setTextColor(resources.getColor(R.color.black))
        } else {
            birthRule.textView.setTextColor(resources.getColor(R.color.red))
        }
    }

    private fun updatePasswordRuleView(
        ruleView: com.umc.myapplication.databinding.ViewPasswordRulesBinding,
        isValid: Boolean
    ) {
        ruleView.icon.visibility = if (isValid) View.GONE else View.VISIBLE
        val color = if (isValid) R.color.black else R.color.gray600
        ruleView.textView.setTextColor(resources.getColor(color))
    }

    // 이 Fragment의 버튼 활성화는 여기서만 결정
    private fun updateStartButtonEnabled() {
        val enabled =
                localFirstNameValid &&
                localLastNameValid &&
                localBirthValid &&
                localPwLenValid &&
                localPwRuleValid&&
                binding.privacyPolicySummaryCheckBox.isChecked
        viewModel.setButtonEnabled(enabled)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
