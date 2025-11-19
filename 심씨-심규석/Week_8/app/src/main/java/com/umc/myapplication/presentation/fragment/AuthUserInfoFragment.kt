package com.umc.myapplication.presentation.fragment

import androidx.fragment.app.Fragment
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.umc.myapplication.R
import com.umc.myapplication.databinding.FragmentAuthUserInfoBinding
import com.umc.myapplication.databinding.ViewPasswordRulesBinding
import com.umc.myapplication.presentation.feature.AuthViewModel
import com.umc.myapplication.presentation.utils.containsUpperLowerAndDigit
import com.umc.myapplication.presentation.utils.isValidBirth
import com.umc.myapplication.presentation.utils.isValidLength
import kotlinx.coroutines.launch

class AuthUserInfoFragment : Fragment() {

    private val viewModel by activityViewModels<AuthViewModel>()
    private var _binding: FragmentAuthUserInfoBinding? = null
    private val binding get() = _binding!!

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

        setupInputListeners()
        bindViewModelStates()
        updateStartButtonEnabled()

        return binding.root
    }

    // ... setupPasswordRules, setupPrivacyPolicy 동일

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

        privacyPolicySummaryCheckBox.setOnCheckedChangeListener { _, _ ->
            updateStartButtonEnabled()
        }


    }

    private fun bindViewModelStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // 회원가입 검증/요청 에러 → 적절한 필드에 표시
                launch {
                    viewModel.signUpError.observe(viewLifecycleOwner) { msg ->
                        // 단일 메시지인 경우 공통 배치. 필요하면 필드별 매핑 로직으로 확장
                        // 예시: 비밀번호 관련 에러면 passwordInputLayout.error에 붙이는 식으로 분기
                        if (msg.isNullOrBlank()) {
                            binding.passwordInputLayout.error = null
                            binding.birthInputLayout.error = null
                            binding.firstNameLayout.error = null
                            binding.lastNameLayout.error = null
                        } else {
                            // 기본은 상단 또는 비밀번호 입력에 표시
                            binding.passwordInputLayout.error = msg
                        }
                    }
                }
                // 회원가입 성공 시 다음 화면으로 진행 가능
                launch {
                    viewModel.signedUpUser.observe(viewLifecycleOwner) { user ->
                        if (user != null) {
                            // findNavController().navigate(R.id.action_authUserInfo_to_next)
                        }
                    }
                }
            }
        }
    }

    private fun handlePasswordInput(password: String) = with(binding) {
        updatePasswordRuleView(passwordRuleMinLength, isValidLength(password))
        updatePasswordRuleView(passwordRuleMixedCaseAndDigit, containsUpperLowerAndDigit(password))
    }

    private fun handleBirthInput(date: String) = with(binding) {
        val ok = date.isNotBlank() && isValidBirth(date)
        birthRule.textView.setTextColor(resources.getColor(if (ok) R.color.black else R.color.red))
    }

    private fun updatePasswordRuleView(
        ruleView: ViewPasswordRulesBinding,
        isValid: Boolean
    ) {
        ruleView.icon.visibility = if (isValid) View.GONE else View.VISIBLE
        val color = if (isValid) R.color.black else R.color.gray600
        ruleView.textView.setTextColor(resources.getColor(color))
    }

    private fun computeLocalEnabled(): Boolean {
        return localFirstNameValid &&
                localLastNameValid &&
                localBirthValid &&
                localPwLenValid &&
                localPwRuleValid &&
                binding.privacyPolicySummaryCheckBox.isChecked
    }

    private fun updateStartButtonEnabled() {
        val enabled = computeLocalEnabled()
        viewModel.setButtonEnabled(enabled)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
